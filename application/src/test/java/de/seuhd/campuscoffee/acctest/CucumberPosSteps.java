package de.seuhd.campuscoffee.acctest;

import de.seuhd.campuscoffee.api.dtos.PosDto;
import de.seuhd.campuscoffee.domain.model.CampusType;
import de.seuhd.campuscoffee.domain.model.PosType;
import de.seuhd.campuscoffee.domain.ports.PosService;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;




import static de.seuhd.campuscoffee.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the POS Cucumber tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberPosSteps {
    static final PostgreSQLContainer<?> postgresContainer;

    static {
        // share the same testcontainers instance across all Cucumber tests
        postgresContainer = getPostgresContainer();
        postgresContainer.start();
        // testcontainers are automatically stopped when the JVM exits
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        configurePostgresContainers(registry, postgresContainer);
    }

    @Autowired
    protected PosService posService;

    @LocalServerPort
    private Integer port;

    @Before
    public void beforeEach() {
        posService.clear();
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @After
    public void afterEach() {
        posService.clear();
    }

    private List<PosDto> createdPosList;
    private PosDto updatedPos;

    private PosDto getPosByName(String name) {
        return createdPosList.stream()
                .filter(pos -> pos.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("POS not found: " + name));
    }


    /**
     * Register a Cucumber DataTable type for PosDto.
     * @param row the DataTable row to map to a PosDto object
     * @return the mapped PosDto object
     */
    @DataTableType
    @SuppressWarnings("unused")
    public PosDto toPosDto(Map<String,String> row) {
        return PosDto.builder()
                .name(row.get("name"))
                .description(row.get("description"))
                .type(PosType.valueOf(row.get("type")))
                .campus(CampusType.valueOf(row.get("campus")))
                .street(row.get("street"))
                .houseNumber(row.get("houseNumber"))
                .postalCode(Integer.parseInt(row.get("postalCode")))
                .city(row.get("city"))
                .build();
    }

    // Given -----------------------------------------------------------------------

    @Given("an empty POS list")
    public void anEmptyPosList() {
        List<PosDto> retrievedPosList = retrievePos();
        assertThat(retrievedPosList).isEmpty();
    }

    // TODO: Add Given step for new scenario

    @Given("I insert the following three POS")
    public void i_insert_the_following_three_pos(List<PosDto> posList) {
        createdPosList = createPos(posList);
        assertThat(createdPosList).size().isEqualTo(3);
    }

    // When -----------------------------------------------------------------------

    @When("I insert POS with the following elements")
    public void insertPosWithTheFollowingValues(List<PosDto> posList) {
        createdPosList = createPos(posList);
        assertThat(createdPosList).size().isEqualTo(posList.size());
    }

    // TODO: Add When step for new scenario

    @When("I update the description of POS {string} to {string}")
    public void i_update_the_description_of_pos_to(String name, String newDescription) {
        PosDto pos = getPosByName(name); // TestUtils içindeki yardımcı metod
        pos.setDescription(newDescription);
        updatePos(pos);                  // TestUtils içindeki metod ile güncelle
        updatedPos = pos;
    }

    // Then -----------------------------------------------------------------------

    @Then("the POS list should contain the same elements in the same order")
    public void thePosListShouldContainTheSameElementsInTheSameOrder() {
        List<PosDto> retrievedPosList = retrievePos();
        assertThat(retrievedPosList)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt", "updatedAt")
                .containsExactlyInAnyOrderElementsOf(createdPosList);
    }

    // TODO: Add Then step for new scenario
    @Then("the description of POS {string} should be {string}")
    public void the_description_of_pos_should_be(String name, String expectedDescription) {
        PosDto pos = getPosByName(name);
        assertThat(pos.getDescription()).isEqualTo(expectedDescription);
    }
    public static List<PosDto> retrievePos() {
        return RestAssured.given()
                .contentType("application/json")
                .get("/api/pos")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", PosDto.class);
    }

    public static void updatePos(PosDto pos) {
        RestAssured.given()
                .contentType("application/json")
                .body(pos)
                .put("/api/pos/" + pos.getId())
                .then()
                .statusCode(200);
    }

}
