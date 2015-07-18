package nl.hugojanssen.sqlgraph.visitors;

import gudusoft.gsqlparser.nodes.TParseTreeVisitor;

public class SQLVisitor extends TParseTreeVisitor
{
	private final VisitorListener parent;

	public SQLVisitor( VisitorListener aInterface )
	{
		this.parent = aInterface;
	}

	public VisitorListener getListener()
	{
		return this.parent;
	}
}
