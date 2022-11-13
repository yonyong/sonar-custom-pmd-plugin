# custom rule
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
# develop a rule

```$xslt
project: sonar-pmd-custom-rules

1. add Rule like top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRule
2. add report err message in src\main\resources\messages.xml and messages_en.xml
3. register rule in src\main\resources\rulesets\java\custom.xml
```
# test your rule

```$xslt
project: sonar-pmd-custom-rules

1. add test class like test/src/java/top.yonyong.sonar.pmd.lang.java.rule.custom.MyFirstTestRuleTest
2. add target test xml: src\test\resources\top\yonyong\sonar\pmd\lang\java\rule\custom\xml\MyFirstTestRule.xml
3. edit your test case in test xml
4. test it ! 
```
# add rule in plugin

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