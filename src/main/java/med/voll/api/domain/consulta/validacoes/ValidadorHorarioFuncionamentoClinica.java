package med.voll.api.domain.consulta.validacoes;

import java.time.DayOfWeek;

import org.springframework.stereotype.Component;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;

@Component
public class ValidadorHorarioFuncionamentoClinica implements IValidadorAgendamentoDeConsulta {

    public void validar(DadosAgendamentoConsulta dados){
        var dataConsulta = dados.data();

        var domingo = dataConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        var antesDaAberturaDaClinica = dataConsulta.getHour() < 7;
        var depoisDaAberturaDaClinica = dataConsulta.getHour() > 18;
        if(domingo || antesDaAberturaDaClinica || depoisDaAberturaDaClinica){
            throw new ValidacaoException("Consulta fora do horario de funcionamento da Clinica!");
        }
    }

}
