package ${basePackage}.service.impl;

import ${basePackage}.mapper.${trueModelNameUpperCamel}Mapper;
import ${basePackage}.model.${trueModelNameUpperCamel};
import ${basePackage}.service.${modelNameUpperCamel}Service;
import ${basePackage}.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by ${author} on ${date}.
 */
@Service
@Transactional
public class ${modelNameUpperCamel}ServiceImpl extends AbstractService<${trueModelNameUpperCamel}> implements ${modelNameUpperCamel}Service {
    @Resource
    private ${trueModelNameUpperCamel}Mapper ${modelNameLowerCamel}Mapper;

}
