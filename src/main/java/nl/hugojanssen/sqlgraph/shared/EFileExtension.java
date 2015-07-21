package nl.hugojanssen.sqlgraph.shared;

/**
 * @author hjanssen
 */
public enum EFileExtension
{
	/**
	 * The file extension of GEXF files
	 */
	EXTENSION_GEXF( "gexf" ),

	/**
	 * The file extension of PDF files
	 */
	EXTENSION_PDF( "pdf" ),

	/**
	 * The file extension of SQL files
	 */
	EXTENSION_SQL( "sql" );

	private final String extension;

	private EFileExtension( String aExtension )
	{
		this.extension = aExtension;
	}

	/**
	 * Returns the string value of the extension.
	 * 
	 * @return the string value of the extension
	 */
	public String extension()
	{
		return this.extension;
	}

	/**
	 * Finds an <code>EFileExtension</code> object by its value.
	 * 
	 * @param value the value to find the <code>EFileExtension</code> object for
	 * @return the <code>EFileExtension</code> object with the specified value if found, null otherwise
	 */
	public static EFileExtension find( String value )
	{
		for ( EFileExtension v : values() )
		{
			if ( v.extension().equals( value ) )
			{
				return v;
			}
		}
		return null;
	}
}
