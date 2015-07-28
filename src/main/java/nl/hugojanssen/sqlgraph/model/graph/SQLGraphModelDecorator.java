package nl.hugojanssen.sqlgraph.model.graph;

import java.awt.Color;

import org.apache.log4j.Logger;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.Workspace;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

public class SQLGraphModelDecorator
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLGraphModelDecorator.class );

	public static void decorate( Workspace workspace )
	{
		LOG.info( "Export graph" );

		GraphModel graphModel = Lookup.getDefault().lookup( GraphController.class ).getModel();
		Graph graph = graphModel.getGraph();

		LOG.info( graph.getNodeCount() );
		LOG.info( graph.getEdgeCount() );

		// TODO: switch labels on by default
		// TODO: opacity?
		// TODO: show labels by default
		// TODO: set gexf version 1.2
		// TODO: set default edge/graph type DIRECTED
		// TODO: YifanHu multilevel(?) layout

		AttributeModel attributeModel = Lookup.getDefault().lookup( AttributeController.class ).getModel();
		RankingController rankingController = Lookup.getDefault().lookup( RankingController.class );

		//		PreviewModel previewModel = Lookup.getDefault().lookup( PreviewController.class ).getModel();
		PreviewController previewController = Lookup.getDefault().lookup( PreviewController.class );
		PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue( PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE );
		previewModel.getProperties().putValue( PreviewProperty.DIRECTED, Boolean.TRUE );
		previewModel.getProperties().putValue( PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE );
		previewModel.getProperties().putValue( PreviewProperty.EDGE_COLOR, new EdgeColor( Color.BLUE ) );

		//		PreviewController previewController = Lookup.getDefault().lookup( PreviewController.class );
		//		PreviewModel previewModel = previewController.getModel();
		previewModel.getProperties().putValue( PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE );
		previewModel.getProperties().putValue( PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor( Color.WHITE ) );
		previewModel.getProperties().putValue( PreviewProperty.EDGE_CURVED, Boolean.TRUE );
		previewModel.getProperties().putValue( PreviewProperty.EDGE_OPACITY, 50 );
		previewModel.getProperties().putValue( PreviewProperty.EDGE_RADIUS, 10f );
		previewModel.getProperties().putValue( PreviewProperty.BACKGROUND_COLOR, Color.BLACK );
		previewController.refreshPreview();

		previewController.refreshPreview( workspace );

		//		VizController.getInstance().getTextManager().getModel().setShowNodeLabels( true );

		layout( graphModel );
		setNodeSize( graphModel, attributeModel, rankingController );
		setNodeColor( rankingController );
		preview( previewModel );

		PreviewProperty[] props = previewModel.getProperties().getProperties();
		for ( PreviewProperty p : props )
		{
			LOG.debug( p.getSource().getClass().getSimpleName() + " -- " + p.getName() + " == " + p.getValue() );
		}
	}

	private static void layout( GraphModel graphModel )
	{
		LOG.debug( "Layout" );
		//Run YifanHuLayout for 100 passes - The layout always takes the current visible view
		YifanHuLayout layout = new YifanHuLayout( null, new StepDisplacement( 1f ) );
		layout.setGraphModel( graphModel );
		layout.resetPropertiesValues();
		layout.setOptimalDistance( 200f );

		layout.initAlgo();
		for ( int i = 0; i < 100 && layout.canAlgo(); i++ )
		{
			layout.goAlgo();
		}
		layout.endAlgo();
	}

	private static void setNodeSize( GraphModel graphModel, AttributeModel attributeModel, RankingController rankingController )
	{
		LOG.debug( "Node size" );

		//Get Centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected( true );
		distance.execute( graphModel, attributeModel );

		//Rank size by centrality
		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn( GraphDistance.BETWEENNESS );
		Ranking centralityRanking = rankingController.getModel().getRanking( Ranking.NODE_ELEMENT, centralityColumn.getId() );
		//		Ranking degreeRanking = rankingController.getModel().getRanking( Ranking.NODE_ELEMENT, Ranking.INDEGREE_RANKING );
		AbstractSizeTransformer sizeTransformer =
			(AbstractSizeTransformer) rankingController.getModel().getTransformer( Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE );
		sizeTransformer.setMinSize( 3 );
		sizeTransformer.setMaxSize( 10 );
		rankingController.transform( centralityRanking, sizeTransformer );
		//		rankingController.transform( degreeRanking, sizeTransformer );
	}

	private static void setNodeColor( RankingController rankingController )
	{
		LOG.debug( "Node color" );

		//Rank color by Degree
		Ranking degreeRanking = rankingController.getModel().getRanking( Ranking.NODE_ELEMENT, Ranking.INDEGREE_RANKING );
		AbstractColorTransformer colorTransformer =
			(AbstractColorTransformer) rankingController.getModel().getTransformer( Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR );
		colorTransformer.setColors( new Color[] { new Color( 0xFEF0D9 ), new Color( 0xB30000 ) } );
		rankingController.transform( degreeRanking, colorTransformer );
	}

	private static void preview( PreviewModel model )
	{
		//Preview
		//		model.getNodeSupervisor().setShowNodeLabels( Boolean.TRUE );
		//		ColorizerFactory colorizerFactory = Lookup.getDefault().lookup( ColorizerFactory.class );
		//		model.getUniEdgeSupervisor().setColorizer( (EdgeColorizer) colorizerFactory.createCustomColorMode( Color.LIGHT_GRAY ) ); //Set edges gray
		//		model.getBiEdgeSupervisor().setColorizer( (EdgeColorizer) colorizerFactory.createCustomColorMode( Color.GRAY ) ); //Set mutual edges red
		//		model.getUniEdgeSupervisor().setEdgeScale( 0.1f );
		//		model.getBiEdgeSupervisor().setEdgeScale( 0.1f );
		//		model.getNodeSupervisor().setBaseNodeLabelFont( model.getNodeSupervisor().getBaseNodeLabelFont().deriveFont( 8 ) );
	}

	private static void setLabelSize()
	{
		LOG.debug( "Label size" );
	}

	private static void setLabelColor()
	{
		LOG.debug( "Label color" );
	}
}