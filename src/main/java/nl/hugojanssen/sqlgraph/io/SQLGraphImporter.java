package nl.hugojanssen.sqlgraph.io;

import java.io.File;
import java.io.FileNotFoundException;
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

/**
 * Imports a graph from the specified file and appends it to the current workspace.
 * 
 * @author hjanssen
 */
public class SQLGraphImporter
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLGraphImporter.class );

	/**
	 * Returns an instance of the <code>SQLGraphImporter</code> object and creates a new object if it does not exist.
	 * 
	 * @return an instance of the <code>SQLGraphImporter</code> object
	 */
	public static SQLGraphImporter getInstance()
	{
		if ( instance == null )
		{
			instance = new SQLGraphImporter();
		}
		return instance;
	}

	private ImportController importController = Lookup.getDefault().lookup( ImportController.class );

	private Workspace workspace;

	private static SQLGraphImporter instance;

	private Container container;

	// Constructor must be protected or private to prevent creating new object
	protected SQLGraphImporter()
	{
		this.setUp();
	}

	/**
	 * Imports a graph from the specified file.
	 * 
	 * @param aFile the file to import the graph from
	 * @throws IllegalArgumentException when the file is undefined (null) or not an GEXF file (but a directory or file
	 *             with an extension other than .gexf)
	 * @throws IOException when the file does not exists or is not readable
	 */
	public void importGraph( File aFile ) throws IOException, IllegalArgumentException
	{
		LOG.debug( "Read graph from file " + aFile );
		SQLParserUtil.validateGEXFFile( aFile );

		try
		{
			this.container = this.importController.importFile( aFile );
		}
		catch ( FileNotFoundException e )
		{
			LOG.error( "Unable to load graph: " + e.getMessage() );
		}
		this.process();
	}

	private void process()
	{
		if ( this.container != null )
		{
			//Append imported data to GraphAPI
			this.container.getLoader().setEdgeDefault( EdgeDefault.DIRECTED ); //Force DIRECTED
			this.importController.process( this.container, new DefaultProcessor(), this.workspace );
		}
	}

	private void setUp()
	{
		this.workspace = SQLGraphModel.getInstance().getCurrentWorkspace();
	}
}