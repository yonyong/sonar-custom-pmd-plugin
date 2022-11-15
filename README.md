*_[EN](https://github.com/yonyong/sonar-custom-pmd-plugin/blob/master/README_EN.md) | 中文_*
# 前言
## - 项目
自定义sonarPMD插件-骨架
## - 介绍
目前sonar基于各版本的自定义PMD插件兼容性不太理想。
因此，针对7.9版本sonar实现了一个自定义sonarPMD插件的骨架项目。  
主要基于下面两个项目进行源码修改：  
https://github.com/alibaba/p3c/tree/master/p3c-pmd  
https://github.com/jborgers/sonar-pmd/tree/master
## - 适用版本
sonar版本7.9
## - 模块介绍
integration-test 集成测试模块  
sonar-pmd-custom-rules 自定义规则模块  
sonar-pmd-plugin 生成sonar插件模块
## - 其他
sonar-pmd-custom-rules模块负责自定义sonar规则，规则编写验证完毕后，install到本地仓。  
sonar-pmd-plugin模块负责集成sonar-pmd-custom-rules模块，打包生成sonar插件。

如果不需要额外定义规则集，从[2.开发一个自定义规则](#idname)开始即可。
# 1. 自定义规则集
```$xslt
模块: sonar-pmd-custom-rules

1. 在/resources/rulesets/java下的ali-set.xml后面新增一个文件 custom.xm
2. 修改pom文件，添加规则集：
<ruleset...</ruleset>
<ruleset>rulesets/java/custom.xml</ruleset>
3. 修改pom文件,在插件中添加依赖, 代码片段如下:
<dependencies>
  <dependency>
    <groupId>com.alibaba.p3c</groupId>
    <artifactId>p3c-pmd</artifactId>
    <version>2.0.1</version>
  </dependency>
  <dependency>
    <groupId>top.yonyong</groupId>
    <artifactId>sonar-pmd-custom-rules</artifactId>
    <version>1.0.0</version>
  </dependency>
</dependencies>
4. 修改pom文件，注释掉gpg插件
5. 执行maven命令: clean install 规则集添加完成。
```
# <a href="#idname">2.开发一个自定义规则</a>

```$xslt
模块: sonar-pmd-custom-rules

1. 添加一个自定义规则，例如：top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRule
2. 在 src\main\resources\messages.xml 和 messages_en.xml文件中，新增规则校验失败后报告的信息
3. 在src\main\resources\rulesets\java\custom.xml中注册自定义的规则
```
# 3.测试你的规则

```$xslt
模块: sonar-pmd-custom-rules

1. 在 test/src/java/下添加一个测试类，例如：top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRuleTest
2. 添加一个测试对象xml：src\test\resources\top\yonyong\sonar\pmd\lang\java\rule\custom\xml\MyFirstTestRule.xml
3. 在上一步的测试xml中，编写测试用例、预期结果
4. 执行测试
```
# 4.在sonar中导入你的规则

```$xslt
模块: sonar-pmd-custom-rules
1. 执行maven命令: mvn clean insall

模块: sonar-pmd-plugin
2. 在 src\main\resources\org\sonar\plugins\pmd\rules-custom.xml中添加自定义的规则
3. 在src\main\resources\org\sonar\l10n\pmd.properties中注册规则名称
4. 执行maven命令: mvn clean package
5. 将打包出来的jar包上传至sonar插件目录下(extensions/plugins/)
6. 重启sonar
```