package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.DadosAtualizacaoPaciente;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.DadosDetalhamentoPaciente;
import med.voll.api.domain.paciente.DadosListarPaciente;
import med.voll.api.domain.paciente.Paciente;
import med.voll.api.domain.paciente.PacienteRepository;

@RestController
@RequestMapping("/paciente")
@SecurityRequirement(name = "bearer-key")
public class PacientesController {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	@PostMapping
	@Transactional
	public ResponseEntity cadastraPaciente(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
		var paciente = new Paciente(dados);
		pacienteRepository.save(paciente);
		var uri = uriBuilder.path("/paciente/{id}").buildAndExpand(paciente.getId()).toUri();
		return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
	}
	
	@GetMapping
	public ResponseEntity<Page<DadosListarPaciente>> listar(@PageableDefault(size = 10, sort = "nome", direction = Direction.ASC) Pageable page){
		var pacientes = pacienteRepository.findAllByAtivoTrue(page).map(DadosListarPaciente::new);
		return ResponseEntity.ok(pacientes);
	}

	@PutMapping
	@Transactional
	public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
		var paciente = pacienteRepository.getReferenceById(dados.id());
		paciente.ataulizaPaciente(dados);
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity excluir(@PathVariable Long id){
		var paciente = pacienteRepository.getReferenceById(id);
		paciente.excluir();
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity detalhar(@PathVariable Long id){
		var paciente = pacienteRepository.getReferenceById(id);
		return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
	}
}
