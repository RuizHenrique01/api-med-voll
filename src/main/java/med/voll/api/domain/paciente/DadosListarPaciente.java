package med.voll.api.domain.paciente;

public record DadosListarPaciente(Long id, String nome, String email, String cpf) {
	public DadosListarPaciente(Paciente paciente) {
		this(paciente.getId(), paciente.getNome(), paciente.getEmail(), paciente.getCpf());
	}
}
