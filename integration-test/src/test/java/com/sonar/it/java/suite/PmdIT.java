/*
 * SonarQube PMD Plugin Integration Test
 * Copyright (C) 2013-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package com.sonar.it.java.suite;

import java.util.ArrayList;
import java.util.List;

import com.sonar.it.java.suite.orchestrator.PmdTestOrchestrator;
import com.sonar.orchestrator.build.BuildResult;
import com.sonar.orchestrator.build.MavenBuild;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sonar.wsclient.issue.Issue;
import org.sonar.wsclient.issue.IssueQuery;

import static com.sonar.it.java.suite.TestUtils.keyFor;
import static com.sonar.it.java.suite.TestUtils.keyForTest;
import static org.assertj.core.api.Assertions.assertThat;

class PmdIT {

    private static final PmdTestOrchestrator ORCHESTRATOR = PmdTestOrchestrator.init();

    @BeforeAll
    static void startSonar() {
        ORCHESTRATOR.start();
    }

    @AfterEach
    void resetData() {
        ORCHESTRATOR.resetData();
    }

    @Test
    void pmdExtensions() {
        final String projectName = "pmd-extensions";
        MavenBuild build = MavenBuild.create(TestUtils.projectPom(projectName))
                .setCleanSonarGoals()
                .setProperty("sonar.java.binaries", ".");

        ORCHESTRATOR.associateProjectToQualityProfile(projectName, projectName);
        final BuildResult buildResult = ORCHESTRATOR.executeBuild(build);
        final String log = buildResult.getLogs();

        assertThat(log).contains("Start MaximumMethodsCountCheck");
        assertThat(log).contains("End MaximumMethodsCountCheck");

        List<Issue> issues = retrieveIssues(keyFor(projectName, "pmd/", "Errors"));
        assertThat(issues).hasSize(3);
        List<String> messages = new ArrayList<>();
        for (Issue issue : issues) {
            messages.add(issue.message());
        }
        assertThat(messages).containsOnly(
                "Avoid too many methods",
                "A catch statement should never catch throwable since it includes errors.",
                "Avoid if without using brace");
    }

    /**
     * SONAR-3346
     */
    @Test
    void testRuleAvoidDuplicateLiterals() {
        final String projectName = "pmd-avoid-duplicate-literals";
        MavenBuild build = MavenBuild.create(TestUtils.projectPom(projectName))
                .setCleanSonarGoals();

        ORCHESTRATOR.associateProjectToQualityProfile("pmd", projectName);
        ORCHESTRATOR.executeBuild(build);

        List<Issue> issues = ORCHESTRATOR.retrieveIssues(
                IssueQuery.create()
                        .rules("pmd:AvoidDuplicateLiterals")
                        .components(keyFor(projectName, "", "AvoidDuplicateLiterals"))
        );
        assertThat(issues).hasSize(1);
        assertThat(issues.get(0).message()).contains("appears 5 times in this file");
    }

    /**
     * SONAR-1076
     */
    @Test
    void testJunitRules() {
        final String projectName = "pmd-junit-rules";
        MavenBuild build = MavenBuild.create(TestUtils.projectPom(projectName))
                .setCleanSonarGoals();

        ORCHESTRATOR.associateProjectToQualityProfile("pmd-junit", projectName);
        ORCHESTRATOR.executeBuild(build);

        List<Issue> testIssues = retrieveIssues(keyForTest());
        assertThat(testIssues).hasSize(1);
        assertThat(testIssues.get(0).message()).matches("This class name ends with '?Test'? but contains no test cases");
        assertThat(testIssues.get(0).ruleKey()).isEqualTo("pmd-unit-tests:TestClassWithoutTestCases");

        List<Issue> prodIssues = retrieveIssues(keyFor(projectName, "", "ProductionCode"));
        assertThat(prodIssues).hasSize(1);
        assertThat(prodIssues.get(0).message()).contains("Avoid unused private fields such as 'unused'.");
        assertThat(prodIssues.get(0).ruleKey()).isEqualTo("pmd:UnusedPrivateField");
    }

    /**
     * SONARPLUGINS-3318
     */
    @Test
    void pmdShouldHaveAccessToExternalLibrariesInItsClasspath() {
        final String projectName = "pmd-extensions";
        MavenBuild build = MavenBuild.create(TestUtils.projectPom(projectName))
                .setCleanPackageSonarGoals();

        ORCHESTRATOR.associateProjectToQualityProfile(projectName, projectName);
        ORCHESTRATOR.executeBuild(build);

        List<Issue> issues = retrieveIssues(keyFor(projectName, "pmd/", "Bar"));
        assertThat(issues).hasSize(1);
    }

    @Test
    void pmdShouldRunWithAllRulesEnabled() {
        final String projectName = "pmd-extensions";
        MavenBuild build = MavenBuild.create(TestUtils.projectPom(projectName))
                .setCleanPackageSonarGoals();

        ORCHESTRATOR.associateProjectToQualityProfile("pmd-all-rules", projectName);
        ORCHESTRATOR.executeBuild(build);
        List<Issue> issues = retrieveIssues(keyFor(projectName, "pmd/", "Bar"));
        assertThat(issues).isNotEmpty();
    }

    private List<Issue> retrieveIssues(String componentKey) {
        return ORCHESTRATOR.retrieveIssues(IssueQuery.create().components(componentKey));
    }
}
