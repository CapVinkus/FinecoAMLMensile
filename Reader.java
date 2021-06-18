package it.gepo.engine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class Reader implements StepExecutionListener, ItemReader<List<List<Map<String, Object>>>> {
	private HashMap<String, Integer> mappaLista = new HashMap<String, Integer>();
	private Logger logger = Logger.getLogger(Reader.class);
	private String query1;
	private String query2;
	private DataSource dataSource;
	boolean readDone = false;
	private ExecutionContext jobExecutionContext;
	
	@Override
	public List<List<Map<String, Object>>> read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (!readDone) {
			Connection conn1 = null;
			conn1 = dataSource.getConnection();
			conn1.setAutoCommit(false);
			PreparedStatement stmt1 = conn1.prepareStatement(query1);

			List<List<Map<String, Object>>> listaTot = new ArrayList<List<Map<String, Object>>>();

			List<Map<String, Object>> listaDaControllare = new ArrayList<Map<String, Object>>();

			ResultSet rs1 = stmt1.executeQuery();

			while (rs1.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("IDENTIFICATIVO", rs1.getBigDecimal(1));
				map.put("NDG", rs1.getBigDecimal(2));
				map.put("CONTO", rs1.getBigDecimal(3));
				map.put("INTESTATARIO", rs1.getString(4));
				map.put("MITTENTE MAIL", rs1.getString(5));
				map.put("OGGETTO MAIL", rs1.getString(6));
				map.put("NOME FILE MAIL", rs1.getString(7));
				map.put("STATO ATTUALE", rs1.getString(8));

				listaDaControllare.add(map);

			}

			rs1.close();
			stmt1.close();

			Connection conn2 = null;
			conn2 = dataSource.getConnection();
			conn2.setAutoCommit(false);
			PreparedStatement stmt2 = conn2.prepareStatement(query2);

			List<Map<String, Object>> listaDaLavorare = new ArrayList<Map<String, Object>>();

			ResultSet rs = stmt2.executeQuery();

			while (rs.next()) {
				Map<String, Object> mappa = new HashMap<String, Object>();
				mappa.put("IDENTIFICATIVO", rs.getBigDecimal(1));
				mappa.put("NDG", rs.getBigDecimal(2));
				mappa.put("CONTO", rs.getBigDecimal(3));
				mappa.put("INTESTATARIO", rs.getString(4));
				mappa.put("MITTENTE MAIL", rs.getString(5));
				mappa.put("OGGETTO MAIL", rs.getString(6));
				mappa.put("NOME FILE MAIL", rs.getString(7));
				mappa.put("STATO ATTUALE", rs.getString(8));
				listaDaLavorare.add(mappa);

			}
			rs.close();
			stmt2.close();
			listaTot.add(listaDaControllare);
			listaTot.add(listaDaLavorare);                                                                                                                                                            
			mappaLista.put("DA_CONTROLLARE", listaTot.get(0).size());
			mappaLista.put("DA_LAVORARE", listaTot.get(1).size());
			jobExecutionContext.put("controllare", listaDaControllare.size());
			jobExecutionContext.put("lavorare", listaDaLavorare.size());
			readDone = true;
			return listaTot;
		}
		return null;
	}

	@Override
	public ExitStatus afterStep(StepExecution arg0) {
		return null;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
	}

	public String getQuery1() {
		return query1;
	}

	public void setQuery1(String query1) {
		this.query1 = query1;
	}

	public String getQuery2() {
		return query2;
	}

	public void setQuery2(String query2) {
		this.query2 = query2;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public ExecutionContext getJobExecutionContext() {
		return jobExecutionContext;
	}

	public void setJobExecutionContext(ExecutionContext jobExecutionContext) {
		this.jobExecutionContext = jobExecutionContext;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
}
