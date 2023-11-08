package curso.api.rest.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class ServiceRelatorio {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public byte[]gerarRelatorio (String nomeRelatorio, ServletContext servletContext) throws Exception{
		/*Obter conexao com banco de dados*/
		
		Connection connection = jdbcTemplate.getDataSource().getConnection();
		
		
		/*Carregar o caminho do arquivo Jasper*/
		
		
		String caminhoJasper = servletContext.getRealPath("relatorios")
				+File.separator + nomeRelatorio + ".jasper";
		
		/*Gerar o relatorio com os dados e conexão*/
		
		JasperPrint print = JasperFillManager.fillReport(caminhoJasper, new HashMap<>(), connection);
		
		/*Exporta para byte o PDF para fazer download*/
		
		byte[]retorno = JasperExportManager.exportReportToPdf(print);
		
		connection.close();
		
		return retorno ;
	}

}
