package nl.hugojanssen.sqlgraph.io;

import java.io.File;
import java.io.IOException;

import nl.hugojanssen.sqlgraph.model.graph.SQLGraphModel;
import nl.hugojanssen.sqlgraph.model.graph.SQLGraphModelDecorator;
import nl.hugojanssen.sqlgraph.shared.EFileExtension;
import nl.hugojanssen.sqlgraph.shared.SQLParserUtil;

import org.apache.log4j.Logger;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.preview.PDFExporter;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import com.itextpdf.text.PageSize;

public class SQLGraphExporter
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLGraphExporter.class );

	private ExportController ec = Lookup.getDefault().lookup( ExportController.class );

	// could be synchronized
	public static SQLGraphExporter getInstance()
	{
		if ( instance == null )
		{
			instance = new SQLGraphExporter();
		}
		return instance;
	}

	private Workspace workspace;

	private static SQLGraphExporter instance;

	// Constructor must be protected or private to prevent creating new object
	protected SQLGraphExporter()
	{
		this.setUp();
	}

	public void export( File aFile ) throws IOException
	{
		SQLParserUtil.validateFileOrDirectory( aFile );

		EFileExtension extension = SQLParserUtil.getFileExtension( aFile.getName() );

		//		// Layout and all
		//		SQLGraphModelDecorator.decorate( this.workspace );
		//
		switch ( extension )
		{
			case EXTENSION_PDF:
				this.toPDF( aFile );
				break;
			case EXTENSION_GEXF:
				this.toGEXF( aFile );
				break;
			default:
				LOG.warn( "Unknown extension " + extension + " for output file " + aFile.getName() );
				break;
		}
	}

	public void toPDF( File aFile )
	{
		PDFExporter pdfExporter = (PDFExporter) this.ec.getExporter( EFileExtension.EXTENSION_PDF.extension() );
		pdfExporter.setPageSize( PageSize.A0 );
		pdfExporter.setWorkspace( this.workspace );
		this.export( aFile, pdfExporter );
	}

	public void toGEXF( File aFile )
	{
		GraphExporter exporter = (GraphExporter) this.ec.getExporter( EFileExtension.EXTENSION_GEXF.extension() );
		exporter.setExportVisible( true ); //Only exports the visible (filtered) graph
		exporter.setWorkspace( this.workspace );
		this.export( aFile, exporter );
	}

	private void export( File aFile, Exporter exporter )
	{
		// Layout and all
		SQLGraphModelDecorator.decorate( this.workspace );

		try
		{
			this.ec.exportFile( aFile, exporter );
		}
		catch ( IOException ex )
		{
			ex.printStackTrace();
			return;
		}
	}

	//	public void toFile( File aFile )
	//	{
	//		//		LOG.info( "Export graph" );
	//		//		LOG.info( this.graph.getNodeCount() );
	//		//		LOG.info( this.graph.getEdgeCount() );
	//
	//		// TODO: switch labels on by default
	//		// TODO: opacity?
	//		// TODO: show labels by default
	//		// TODO: set gexf version 1.2
	//		// TODO: set default edge/graph type DIRECTED
	//		// TODO: YifanHu multilevel(?) layout
	//
	//		PreviewModel model = Lookup.getDefault().lookup( PreviewController.class ).getModel();
	//		model.getProperties().putValue( PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE );
	//		model.getProperties().putValue( PreviewProperty.DIRECTED, Boolean.TRUE );
	//		model.getProperties().putValue( PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE );
	//
	//		model.getProperties().putValue( PreviewProperty.EDGE_COLOR, new EdgeColor( Color.BLUE ) );
	//
	//		//		VizController.getInstance().getTextManager().getModel().setShowNodeLabels( true );
	//
	//		this.toGEXF( aFile );
	//		this.toPDF( aFile );
	//
	//		//		//Export full graph
	//		//		ExportController ec = Lookup.getDefault().lookup( ExportController.class );
	//		//		GraphExporter exporter = (GraphExporter) ec.getExporter( "gexf" ); //Get GraphML exporter
	//		//		exporter.setExportVisible( true );
	//		//
	//		//		try
	//		//		{
	//		//			ec.exportFile( aFile, exporter );
	//		//		}
	//		//		catch ( Exception ex )
	//		//		{
	//		//			ex.printStackTrace();
	//		//			return;
	//		//		}
	//	}

	private void setUp()
	{
		this.workspace = SQLGraphModel.getInstance().getCurrentWorkspace();
	}
}