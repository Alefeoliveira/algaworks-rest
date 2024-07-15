package br.com.alefeoliveira.domain.service;

import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alefeoliveira.domain.exception.ReportException;
import br.com.alefeoliveira.domain.repository.filter.VendaDiariaFilter;
import jakarta.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class VendaReportServiceImpl implements VendaReportService {
	
	@Autowired
	private EntityManager manager;
	
	@Autowired
	private VendaQueryService vendaQueryService;

	@Override
	public byte[] emitirVendasDiarias(VendaDiariaFilter filter) {
		try {
			var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");
			
			var parametros = new HashMap<String, Object>();
			parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
			
			var vendasDiarias = vendaQueryService.consultarVendasDiarias(filter);
			var dataSource = new JRBeanCollectionDataSource(vendasDiarias);
			
			var jasperPrint = JasperFillManager.fillReport(inputStream, parametros, dataSource);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			throw new ReportException("Não foi possivel emitir o relatorio de vendas Diarias");
		}
	}
}
