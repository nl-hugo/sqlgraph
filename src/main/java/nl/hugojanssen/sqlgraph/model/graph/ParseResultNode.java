package nl.hugojanssen.sqlgraph.model.graph;

import java.awt.Color;

import nl.hugojanssen.sqlgraph.model.ParseResult;
import nl.hugojanssen.sqlgraph.model.sql.EClauseType;

import org.gephi.graph.api.Node;
import org.jcolorbrewer.ColorBrewer;

/**
 * The ParseResult object holds details about the table that was found in a SQL statement by the parser.
 * 
 * @author hjanssen
 */
@SuppressWarnings( "serial" )
public class ParseResultNode extends ParseResult implements NodeRenderProperties
{
	public ParseResultNode( Object source, String schema, String name, EClauseType role, long lineNo, long colNo )
	{
		super( source, schema, name, role, lineNo, colNo );
	}

	public ParseResultNode( ParseResult aResult )
	{
		this( aResult.getSource(), aResult.getSchema(), aResult.getName(), aResult.getRole(), aResult.getLineNo(), aResult.getColNo() );
	}

	@Override
	public float getAlpha()
	{
		return .5f;
	}

	@Override
	public Color getColor()
	{
		Color[] colors = ColorBrewer.PuRd.getColorPalette( 8 );

		//TODO: return color based on table prefix
		return colors[5];
	}

	@Override
	public String getLabel()
	{
		return this.getName();
	}

	@Override
	public float getSize()
	{
		return 15f;
	}

	@Override
	public String getSQLId()
	{
		return this.getId();
	}

	public Node toNode()
	{
		Node node = ModelBuilder.getInstance().newNode( this.getSQLId() );
		node.getNodeData().setLabel( this.getLabel() );
		node.getNodeData().setSize( this.getSize() );
		node.getNodeData().setColor( this.getColor().getRed(), this.getColor().getGreen(), this.getColor().getBlue() );
		node.getNodeData().setAlpha( this.getAlpha() );
		return node;
	}
}