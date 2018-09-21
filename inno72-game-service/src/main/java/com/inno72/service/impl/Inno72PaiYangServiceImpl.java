package com.inno72.service.impl;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.RedisConstants;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.mapper.*;
import com.inno72.model.*;
import com.inno72.service.*;
import com.inno72.vo.Inno72SamplingGoods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class Inno72PaiYangServiceImpl implements Inno72PaiYangService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Inno72PaiYangServiceImpl.class);
    @Autowired
    private Inno72InteractMachineTimeService inno72InteractMachineTimeService;

    @Autowired
    private Inno72InteractService inno72InteractService;
    @Autowired
    private Inno72GameService inno72GameService;

    @Autowired
    private Inno72InteractMachineGoodsService inno72InteractMachineGoodsService;

    @Autowired
    private Inno72SupplyChannelMapper inno72SupplyChannelMapper;

    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

    @Resource
    private Inno72ShopsMapper inno72ShopsMapper;

    @Resource
    private Inno72InteractShopsMapper inno72InteractShopsMapper;
//    @Autowired
//    private Inno72InteractGoodsMapper inno72InteractGoodsMapper;

    @Resource
    private Inno72GameServiceProperties inno72GameServiceProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Result<List<Inno72SamplingGoods>> getSampling(String machineCode) {
        //查询该机器配置的商品信息（当前时间在起止时间内的）
        Inno72InteractMachine interactMachine = inno72InteractMachineTimeService.findActiveInteractMachine(machineCode);
        if(interactMachine == null){
            LOGGER.info("getSampling,此机器无活动配置machineCode={}",machineCode);
            return Results.warn("此机器无活动配置",2);
        }

        String interactId = interactMachine.getInteractId();
        //查找对应的游戏里面的显示条数
        Inno72Interact interact = inno72InteractService.findById(interactId);
        if(interact == null ){
            LOGGER.info("getSampling,此机器动配置的活动不见了machineCode={},interactId = {}",machineCode,interactId);
            return Results.warn("此机器动配置的活动不见了",2);
        }
        if(Inno72Interact.STATUS_COMMITED!=interact.getStatus()){
            LOGGER.info("getSampling,此机器动配置的活动状态不是已提交,machineCode={},interactId = {},status={}",machineCode,interactId,interact.getStatus());
            return Results.warn("此机器动配置的活动状态不是已提交",2);
        }
        String gameId = interact.getGameId();
        Inno72Game game = inno72GameService.findById(gameId);
        if(game == null){
            LOGGER.info("getSampling,此机器动配置的游戏不见了,gameId={}",gameId);
            return Results.warn("此机器动配置的游戏不见了",2);
        }
        Integer goodsSize = game.getMaxGoodsNum();
        LOGGER.debug("getSampling,配置的显示商品个数为：{}",goodsSize);

        //查找此机器配置的所有商品ids
        List<Inno72InteractMachineGoods> inno72InteractMachineGoodsList = inno72InteractMachineGoodsService.findMachineGoods(interactMachine.getId());

        if(inno72InteractMachineGoodsList == null || inno72InteractMachineGoodsList.size() == 0){
            LOGGER.info("getSampling,此机器无商品配置machineCode={},interactId={}",machineCode,interact.getId());
            return Results.warn("此机器无商品配置",2);
        }

        //组装vo
        // 获取存储图片阿里云地址
        String aliyunUrl = inno72GameServiceProperties.get("returnUrl");
        List<Inno72SamplingGoods> list = new ArrayList<Inno72SamplingGoods>(inno72InteractMachineGoodsList.size());
        for(Inno72InteractMachineGoods inno72InteractMachineGoods:inno72InteractMachineGoodsList){

            String goodsId = inno72InteractMachineGoods.getGoodsId();
            Inno72SamplingGoods sampLingGoods = inno72GoodsMapper.findSamplingGoodsById(goodsId);
            Integer goodsCount = getMachineGoodsCount(sampLingGoods.getId(),interactMachine.getMachineId());
            //机器里面的所有货到的此商品数量
            sampLingGoods.setNum(goodsCount);

            //商品的剩余数量（此机器）配置的数量-掉货数量
            Integer number = inno72InteractMachineGoods.getNumber();//机器配置的此商品的数量
            //已经掉货的数量
            String redisKey = String.format(RedisConstants.PAIYANG_MACHINE_GOODS,interactId,interactMachine.getMachineId(),goodsId);
            boolean flag = redisTemplate.hasKey(redisKey);
            if(flag){
                number = number - Integer.parseInt(redisTemplate.opsForValue().get(redisKey));
            }
            sampLingGoods.setMachineSurplusGoodsNum(number);

            // 根据商品id查询相关店铺信息
            Inno72InteractShops inno72InteractShops = inno72InteractShopsMapper.findByInteractIdAndShopId(interactId,sampLingGoods.getShopId());
            if (inno72InteractShops != null) {
                sampLingGoods.setIsVip(inno72InteractShops.getIsVip());
                //获取入会编码
                Inno72Shops inno72Shops = inno72ShopsMapper.selectByPrimaryKey(inno72InteractShops.getShopsId());
                sampLingGoods.setShopName(inno72Shops.getShopName());
                sampLingGoods.setSessionKey(inno72Shops.getSessionKey());
            }

            // \
            if (sampLingGoods.getImg() != null && !"".equals(sampLingGoods.getImg())) {
                sampLingGoods.setImg(aliyunUrl + sampLingGoods.getImg());
            }
            if (sampLingGoods.getBanner() != null && !"".equals(sampLingGoods.getBanner())) {
                sampLingGoods.setBanner(aliyunUrl + sampLingGoods.getBanner());
            }
            list.add(sampLingGoods);
        }
        if(list.size() <= goodsSize) return Results.success(list);
        List<Inno72SamplingGoods> retList = new ArrayList<Inno72SamplingGoods>(goodsSize);
        for(Inno72SamplingGoods inno72SamplingGoods:list){
            if(inno72SamplingGoods.getMachineSurplusGoodsNum()> 0){
                retList.add(inno72SamplingGoods);
                if(retList.size() == goodsSize) return Results.success(retList);
            }
        }
        //如果货物不够5个，展示无货的
        for(int i = list.size()-1;i>=0;i--){
            Inno72SamplingGoods inno72SamplingGoods = list.get(i);
            if(inno72SamplingGoods.getMachineSurplusGoodsNum() == 0){
                retList.add(inno72SamplingGoods);
                if(retList.size() == goodsSize) return Results.success(retList);
            }
        }
        return Results.success(retList);
    }

    private Integer getMachineGoodsCount(String goodId, String machineId) {

        // 根据商品id查询货道
        Map<String, String> channelParam = new HashMap<String, String>();
        channelParam.put("goodId", goodId);
        channelParam.put("machineId", machineId);
        List<Inno72SupplyChannel> inno72SupplyChannels = inno72SupplyChannelMapper
                .selectByGoodsId(channelParam);

        Integer goodsCount = 0;
        if (inno72SupplyChannels != null && inno72SupplyChannels.size() > 0) {
            // 所有具有相同商品id的货道中中道商品数量相加
            for (Inno72SupplyChannel channel : inno72SupplyChannels) {
                goodsCount += channel.getGoodsCount();
            }
        }
        return goodsCount;
    }
}
