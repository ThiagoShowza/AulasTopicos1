package br.unitins.topicos1.resource;

import org.jboss.logging.Logger;

import br.unitins.topicos1.dto.LoginDTO;
import br.unitins.topicos1.dto.UsuarioResponseDTO;
import br.unitins.topicos1.service.HashService;
import br.unitins.topicos1.service.JwtService;
import br.unitins.topicos1.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UsuarioService service;

    @Inject
    HashService hashService;

    @Inject
    JwtService jwtService;

    // IMPORT Jboss

    private static final Logger LOG = Logger.getLogger(AuthResource.class);

    @POST
    public Response login(@Valid LoginDTO dto) {
        LOG.infof("Iniciano a autenticação do %s", dto.login()); //Mostrar etapas do login

        String hashSenha = hashService.getHashSenha(dto.senha());
        LOG.info("Hash da senha gerado");
        LOG.debug(hashSenha);

        UsuarioResponseDTO result = service.findByLoginAndSenha(dto.login(), hashSenha);

        if (result != null) {
            LOG.info("Login e senha corretos");

        }else{
            LOG.info("Login e senha incorretos");
        }

        String token = jwtService.generateJwt(result);

// \\ falta coisa

        return Response.ok().header("Authorization", token).build();
    }

}
