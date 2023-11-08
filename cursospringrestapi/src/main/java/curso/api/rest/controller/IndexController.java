package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;
import curso.api.rest.repositoy.TelefoneRepository;
import curso.api.rest.repositoy.UsuarioRepository;
import curso.api.rest.service.ImplementacaoUserDetailsSercice;
import curso.api.rest.service.ServiceRelatorio;

@RestController /* Arquitetura REST */
@RequestMapping(value = "/usuario")
public class IndexController {

	@Autowired /* de fosse CDI seria @Inject */
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TelefoneRepository telefoneRepository;

	@Autowired
	private ServiceRelatorio serviceRelatorio;

	@Autowired
	private ImplementacaoUserDetailsSercice implementacaoUserDetailsSercice;

	/* Serviço RESTful */
	@GetMapping(value = "/{id}/codigovenda/{venda}", produces = "application/json")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id,
			@PathVariable(value = "venda") Long venda) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		/* o retorno seria um relatorio */
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	/* Serviço RESTful */
	@GetMapping(value = "/{id}", produces = "application/json")
	@CachePut("cacheuser")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return "ok";
	}

	@DeleteMapping(value = "/{id}/venda", produces = "application/text")
	public String deletevenda(@PathVariable("id") Long id) {

		usuarioRepository.deleteById(id);

		return "ok";
	}

	/*
	 * Vamos supor que o carregamento de usuário seja um processo lento e queremos
	 * controlar ele com cache para agilizar o processo
	 */
	@GetMapping(value = "/", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuario() throws InterruptedException {

		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));

		Page<Usuario> list = usuarioRepository.findAll(page);

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarioPagina(@PathVariable("pagina") int pagina) throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));

		Page<Usuario> list = usuarioRepository.findAll(page);

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	/* END-POINT consulta de usuário por nome */
	@GetMapping(value = "/usuarioPorNome/{nome}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarioPorNome(@PathVariable("nome") String nome) throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	/* END-POINT consulta de usuário por nome */
	@GetMapping(value = "/usuarioPorNome/{nome}/page/{page}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarioPorNomePage(@PathVariable("nome") String nome,
			@PathVariable("page") int page) throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty())
				|| nome.equalsIgnoreCase("undefined")) {/* Não informou nome */

			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(page, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);
	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody @Valid Usuario usuario) {

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		implementacaoUserDetailsSercice.insereAcessoPadrao(usuarioSalvo.getId());

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		/* outras rotinas antes de atualizar */

		for (int pos = 0; pos < usuario.getTelefones().size(); pos++) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}

		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();

		if (!userTemporario.getSenha().equals(usuario.getSenha())) { /* Senhas diferentes */
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}

		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@PutMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
	public ResponseEntity updateVenda(@PathVariable Long iduser, @PathVariable Long idvenda) {
		/* outras rotinas antes de atualizar */

		// Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity("Venda atualzada", HttpStatus.OK);

	}

	@PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces = "application/json")
	public ResponseEntity cadastrarvenda(@PathVariable Long iduser, @PathVariable Long idvenda) {

		/* Aqui seria o processo de venda */
		// Usuario usuarioSalvo = usuarioRepository.save(usuario);

		return new ResponseEntity("id user :" + iduser + " idvenda :" + idvenda, HttpStatus.OK);

	}

	@DeleteMapping(value = "/removerTelefone/{id}", produces = "application/text")
	public String deleteTelefone(@PathVariable("id") Long id) {

		telefoneRepository.deleteById(id);

		return "ok";
	}

	@GetMapping(value = "/relatorio", produces = "application/text")
	public ResponseEntity<String> donwloadRelatorio(HttpServletRequest request) throws Exception {

		byte[] pdf = serviceRelatorio.gerarRelatorio("relatorio-usuario", request.getServletContext());

		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);

	}

}
