/**
 * 
 */
package nl.hugojanssen.sqlgraph.model.graph;

import java.awt.Color;

/**
 * @author hjanssen
 */
interface RenderProperties
{
	public String getSQLId();

	public String getLabel();

	public float getAlpha();

	public Color getColor();
}
