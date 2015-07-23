package nl.hugojanssen.sqlgraph.io;

import java.io.File;
import java.io.IOException;

import nl.hugojanssen.sqlgraph.model.graph.SQLGraphModel;
import nl.hugojanssen.sqlgraph.shared.SQLParserUtil;

import org.apache.log4j.Logger;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.EdgeDefault;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class SQLGraphImporter
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLGraphImporter.class );

	private ImportController importController = Lookup.getDefault().lookup( ImportController.class );

	// could be synchronized
	public static SQLGraphImporter getInstance()
	{
		if ( instance == null )
		{
			instance = new SQLGraphImporter();
		}
		return instance;
	}

	private Workspace workspace;

	private static SQLGraphImporter instance;

	// Constructor must be protected or private to prevent creating new object
	protected SQLGraphImporter()
	{
		this.setUp();
	}

	public void importGraph( File aFile ) throws IOException
	{
		SQLParserUtil.validateFileOrDirectory( aFile );

		//Import file       
		Container container;
		try
		{
			container = this.importController.importFile( aFile );
			container.getLoader().setEdgeDefault( EdgeDefault.DIRECTED ); //Force DIRECTED
		}
		catch ( Exception ex )
		{
			ex.printStackTrace();
			return;
		}

		//Append imported data to GraphAPI
		this.importController.process( container, new DefaultProcessor(), this.workspace );

		//See if graph is well imported
		System.out.println( "Nodes: " + SQLGraphModel.getInstance().getNodeCount() );
		System.out.println( "Edges: " + SQLGraphModel.getInstance().getEdgeCount() );
	}

	private void setUp()
	{
		this.workspace = SQLGraphModel.getInstance().getCurrentWorkspace();
	}
}