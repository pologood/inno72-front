package com.inno72.common;

/**
 * redis key常量
 */
public class RedisConstants {
    // 派样活动id:机器:商品维度的掉货数量(获取展示商品时候剩余数量用)
    public final static String PAIYANG_MACHINE_GOODS = "paiyang:%s:%s:%s";

//    public final static String PAIYANG_DAY_GAME_TIMES = "paiyang_day_game_times:%s:%s:%s";//活动id:日期:用户id
//
//    public final static String PAIYANG_GAME_TIMES = "paiyang_game_times:%s:%s";//活动id:用户id

    public final static String PAIYANG_DAY_ORDER_TIMES = "paiyang_day_order_times:%s:%s:%s";//活动id:日期:用户id

    public final static String PAIYANG_ORDER_TIMES = "paiyang_order_times:%s:%s";//活动id:用户id

    public final static String PAIYANG_GOODS_ORDER_TIMES = "paiyang_goods_order_times:%s:%s:%s%s";//活动id:商品id:日期:用户id
}
