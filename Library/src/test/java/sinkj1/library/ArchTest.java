package sinkj1.library;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("sinkj1.library");

        noClasses()
            .that()
            .resideInAnyPackage("sinkj1.library.service..")
            .or()
            .resideInAnyPackage("sinkj1.library.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..sinkj1.library.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
