package nl.hugojanssen.sqlgraph.model.sql;

import org.apache.log4j.Logger;

public class SQLTable
{
	/** The logger */
	private final static Logger LOG = Logger.getLogger( SQLTable.class );

	private String schema;

	private final String name;

	/**
	 * @param model
	 * @param schema
	 * @param name
	 */
	public SQLTable( String schema, String name )
	{
		LOG.debug( "Create new SQLTable " + name );
		this.schema = schema;
		this.name = name;

		this.validateState();
	}

	private void validateState()
	{
		//TODO: name cannot be null
		if ( this.schema == null || this.schema.equals( "" ) )
		{
			this.schema = SQLConstants.DEFAULT_SCHEMA;
			LOG.warn( "No schema specified, assuming default " + this.schema );
		}
	}

	@Override
	public String toString()
	{
		return "[" + this.getSchema() + "].[" + this.getName() + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( this.name == null ) ? 0 : this.name.hashCode() );
		result = prime * result + ( ( this.schema == null ) ? 0 : this.schema.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
		{
			return true;
		}
		if ( obj == null )
		{
			return false;
		}
		if ( this.getClass() != obj.getClass() )
		{
			return false;
		}
		SQLTable other = (SQLTable) obj;
		if ( this.name == null )
		{
			if ( other.name != null )
			{
				return false;
			}
		}
		else if ( !this.name.equals( other.name ) )
		{
			return false;
		}
		if ( this.schema == null )
		{
			if ( other.schema != null )
			{
				return false;
			}
		}
		else if ( !this.schema.equals( other.schema ) )
		{
			return false;
		}
		return true;
	}

	public String getName()
	{
		return this.name;
	}

	public String getSchema()
	{
		return this.schema;
	}
}