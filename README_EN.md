*_EN | [中文](https://github.com/yonyong/sonar-custom-pmd-plugin/blob/master/README.md)_*
# Preface
## - Project
Custom sonar PMD plugin - arch
## - Introduction
Currently sonar is not compatible with custom PMD plugins based on various versions.  
Therefore, a skeleton project of custom sonar PMD plug-in has been implemented for version 7.9 of sonar.    
Source code changes are mainly based on the following two projects:    
https://github.com/alibaba/p3c/tree/master/p3c-pmd  
https://github.com/jborgers/sonar-pmd/tree/master
## - Suitable version
sonar 7.9+
## - Module Introduction
integration-test:integration-test module  
sonar-pmd-custom-rules:custom rule module    
sonar-pmd-plugin:Generate the sonar plugin module
## - Other
sonar-pmd-custom-rules module is responsible for customizing sonar rules. After the rules are written and verified, install them into the local repository.    
The sonar-pmd-plugin module is responsible for integrating the sonar-pmd-custom-rules module and packaging to generate the sonar plug-in.  

If you do not need to define additional rule sets, start with [2.develop a rule](idname).
# 1.custom ruleSets
```$xslt
module: sonar-pmd-custom-rules

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
# <a href="#idname">2.develop a rule</a>

```$xslt
module: sonar-pmd-custom-rules

1. add Rule like top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRule
2. add report err message in src\main\resources\messages.xml and messages_en.xml
3. register rule in src\main\resources\rulesets\java\custom.xml
```
# 3.test your rule

```$xslt
module: sonar-pmd-custom-rules

1. add test class like test/src/java/top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRuleTest
2. add target test xml: src\test\resources\top\yonyong\sonar\pmd\lang\java\rule\custom\xml\MyFirstTestRule.xml
3. edit your test case in test xml
4. test it ! 
```
# 4.add rule in plugin

```$xslt
module: sonar-pmd-custom-rules
1. execute command: mvn clean insall

module: sonar-pmd-plugin
2. add rule in src\main\resources\org\sonar\plugins\pmd\rules-custom.xml
3. register ruleName in src\main\resources\org\sonar\l10n\pmd.properties
4. execute command: mvn clean package
5. add jar in sonar plugin dir(extensions/plugins/)
6. restart sonar
```