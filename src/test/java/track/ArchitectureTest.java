package track;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Design invariants (main classes only; tests excluded). Drop or relax rules that don't fit.
 * {@code .that()}-filtered rules use {@code allowEmptyShould} so a single-class solution stays green.
 */
@AnalyzeClasses(packages = "track", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

  /** Time is compared as fixed-width strings; drop this if the challenge needs date math. */
  @ArchTest
  static final ArchRule noJavaTime =
      noClasses().should().dependOnClassesThat().resideInAPackage("java.time..")
          .because("time is handled as lexicographic strings");

  /** Deterministic: no randomness in the solution. */
  @ArchTest
  static final ArchRule noRandomness =
      noClasses().should().dependOnClassesThat().haveFullyQualifiedName("java.util.Random")
          .because("input is deterministic");

  /** Keep I/O in the App entry point so domain logic stays pure. */
  @ArchTest
  static final ArchRule ioConfinedToEntryPoint =
      noClasses().that().haveSimpleNameNotStartingWith("App")
          .should().dependOnClassesThat().resideInAPackage("java.io..")
          .because("I/O is confined to the App entry point")
          .allowEmptyShould(true);
}
