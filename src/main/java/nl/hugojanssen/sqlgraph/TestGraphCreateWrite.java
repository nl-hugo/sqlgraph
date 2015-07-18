package nl.hugojanssen.sqlgraph;

import java.io.File;
import java.io.IOException;

import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.Exporter;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class TestGraphCreateWrite
{

	public static void main( String[] args )
	{

		new TestGraphCreateWrite();
	}

	public TestGraphCreateWrite()
	{

		ProjectController pc = Lookup.getDefault().lookup( ProjectController.class );
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		GraphModel model = Lookup.getDefault().lookup( GraphController.class ).getModel();

		Node n0 = model.factory().newNode( "n0" );
		n0.getNodeData().setLabel( "n0" );

		Node n1 = model.factory().newNode( "n1" );
		n1.getNodeData().setLabel( "n1" );

		Edge e1 = model.factory().newEdge( n1, n0, 1f, true );

		DirectedGraph graph = model.getDirectedGraph();
		graph.addNode( n0 );
		graph.addNode( n1 );
		graph.addEdge( e1 );

		System.out.println( graph.getNodeCount() );
		System.out.println( graph.getEdgeCount() );

		//Export full graph
		ExportController ec = Lookup.getDefault().lookup( ExportController.class );
		Exporter exporterGraphML = ec.getExporter( "graphml" ); //Get GraphML exporter
		exporterGraphML.setWorkspace( workspace );
		//StringWriter stringWriter = new StringWriter();
		//ec.exportWriter( stringWriter, (CharacterExporter) exporterGraphML );
		//System.out.println( stringWriter.toString() ); //Uncomment this line
		try
		{
			ec.exportFile( new File( "io_graphml.graphml" ), exporterGraphML );
		}
		catch ( IOException ex )
		{
			ex.printStackTrace();
			return;
		}

		//PDF Exporter config and export to Byte array
		/*PDFExporter pdfExporter = (PDFExporter) ec.getExporter( "pdf" );
		pdfExporter.setPageSize( PageSize.A0 );
		pdfExporter.setWorkspace( workspace );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ec.exportStream( baos, pdfExporter );
		byte[] pdf = baos.toByteArray();
		*/
	}
}
