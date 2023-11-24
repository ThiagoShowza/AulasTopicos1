package br.unitins.topicos1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.unitins.topicos1.dto.TelefoneDTO;
import br.unitins.topicos1.dto.UsuarioDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.service.UsuarioService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
// import jakarta.ws.rs.core.Response;
import io.restassured.response.Response;

@QuarkusTest
public class UsuarioResourceTest {

    @Inject
    UsuarioService usuarioService;

    @Test
    public void testFindAll() {
        given()
                .when().get("/usuarios")
                .then()
                .statusCode(200);
    }

    @Test
    public void testInsert() {
        List<TelefoneDTO> telefones = new ArrayList<TelefoneDTO>();
        telefones.add(new TelefoneDTO("63", "5555-5555"));

        UsuarioDTO dto = new UsuarioDTO(
                "Mark Zuckerberg Insert",
                "marquinho",
                "333",
                1,
                telefones);

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/usuarios")
                .then()
                .statusCode(201)
                .body(
                        "id", notNullValue(),
                        "nome", is("Mark Zuckerberg Insert"),
                        "login", is("marquinho"));
    }

    @Test
    public void testUpdate() {
        List<TelefoneDTO> telefones = new ArrayList<TelefoneDTO>();
        telefones.add(new TelefoneDTO("63", "5555-5555"));

        UsuarioDTO dto = new UsuarioDTO(
                "Mark Zuckerberg Update",
                "marquinho",
                "333",
                1,
                telefones);

        // inserindo um usuario
        UsuarioResponseDTO usuarioTest = usuarioService.insert(dto);
        Long id = usuarioTest.id();

        UsuarioDTO dtoUpdate = new UsuarioDTO(
                "Mark Zuckerberg",
                "mark",
                "555",
                1,
                telefones);

        given()
                .contentType(ContentType.JSON)
                .body(dtoUpdate)
                .when().put("/usuarios/" + id)
                .then()
                .statusCode(204);

        // verificando a alteracao

        // System.out.println(id);
        // System.out.println(id);
        // System.out.println(id);
        // System.out.println(id);
        UsuarioResponseDTO usu = usuarioService.findById(id);
        assertThat(usu.nome(), is("Mark Zuckerberg"));
        assertThat(usu.login(), is("mark"));

    }

    @Test
    public void testDelete() {
        List<TelefoneDTO> telefones = new ArrayList<TelefoneDTO>();
        telefones.add(new TelefoneDTO("63", "5555-5555"));

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                "Mark Zuckerberg Update",
                "marquinho",
                "333",
                1,
                telefones);

        Response insertResponse = given()
                .contentType(ContentType.JSON)
                .body(usuarioDTO)
                .when()
                .post("/usuarios");

        insertResponse.then()
                .statusCode(201);

        Long id = insertResponse.jsonPath().getLong("id");

        given()
                .when()
                .delete("/usuarios/" + id)
                .then()
                .statusCode(204);

    }

}