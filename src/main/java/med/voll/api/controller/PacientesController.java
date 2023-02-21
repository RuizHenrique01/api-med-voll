package med.voll.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.DadosAtualizacaoPaciente;
import med.voll.api.paciente.DadosCadastroPaciente;
import med.voll.api.paciente.DadosListarPaciente;
import med.voll.api.paciente.Paciente;
import med.voll.api.paciente.PacienteRepository;

@RestController
@RequestMapping("/paciente")
public class PacientesController {

	@Autowired
	private PacienteRepository pacienteRepository;
	
	@PostMapping
	@Transactional
	public void cadastraPaciente(@RequestBody @Valid DadosCadastroPaciente dados) {
		pacienteRepository.save(new Paciente(dados));
	}
	
	@GetMapping
	public Page<DadosListarPaciente> listar(@PageableDefault(size = 10, sort = "nome", direction = Direction.ASC) Pageable page){
		return pacienteRepository.findAllByAtivoTrue(page).map(DadosListarPaciente::new);
	}

	@PutMapping
	@Transactional
	public void atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados){
		var paciente = pacienteRepository.getReferenceById(dados.id());
		paciente.ataulizaPaciente(dados);
	}

	@DeleteMapping("/{id}")
	@Transactional
	public void excluir(@PathVariable Long id){
		var paciente = pacienteRepository.getReferenceById(id);
		paciente.excluir();
	}
}
