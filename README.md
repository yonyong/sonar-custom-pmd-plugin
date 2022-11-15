*_[EN](https://github.com/yonyong/sonar-java-custom-plugin/blob/master/README_EN.md) | 中文_*
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
project: sonar-pmd-custom-rules

1. add custom.xml after ali-set.xml in /resources/rulesets/java
2. edit pom.xml,add ruleset,as: 
<ruleset...</ruleset>
<ruleset>rulesets/java/custom.xml</ruleset>
3. edit pom.xml,add dependency in plugin, the snippet of code is finally:
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
4. edit pom.xml,comment out gpg plugin
5. maven run: clean install
```
# <a href="#idname">2.开发一个自定义规则</a>

```$xslt
project: sonar-pmd-custom-rules

1. add Rule like top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRule
2. add report err message in src\main\resources\messages.xml and messages_en.xml
3. register rule in src\main\resources\rulesets\java\custom.xml
```
# 3.测试你的规则

```$xslt
project: sonar-pmd-custom-rules

1. add test class like test/src/java/top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRuleTest
2. add target test xml: src\test\resources\top\yonyong\sonar\pmd\lang\java\rule\custom\xml\MyFirstTestRule.xml
3. edit your test case in test xml
4. test it ! 
```
# 4.在sonar中导入你的规则

```$xslt
project: sonar-pmd-custom-rules
1. execute command: mvn clean insall

project: sonar-pmd-plugin
2. add rule in src\main\resources\org\sonar\plugins\pmd\rules-custom.xml
3. register ruleName in src\main\resources\org\sonar\l10n\pmd.properties
4. execute command: mvn clean package
5. add jar in sonar plugin dir(extensions/plugins/)
6. restart sonar
```