package curso.api.rest.repositoy;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import curso.api.rest.model.Clientes;

@Repository
public interface ClienteRepository extends JpaRepository<Clientes, Long> {

	

	
	@Query("select u from Clientes u where u.nome like %?1%")
	List<Clientes> findUserByNome(String nome);

	

	

	default Page<Clientes> findUserByNamePage(String nome, PageRequest pageRequest) {

		Clientes usuario = new Clientes();
		usuario.setNome(nome);

		/* Configurando para pesquisar por nome e paginação */
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Clientes> example = Example.of(usuario, exampleMatcher);

		Page<Clientes> retorno = findAll(example, pageRequest);

		return retorno;

	}

}
