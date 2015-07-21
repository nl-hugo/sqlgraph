package nl.hugojanssen.sqlgraph.shared;

import java.io.File;
import java.io.FileFilter;

/**
 * Accepts directories and SQL files.
 * 
 * @author hjanssen
 */
public class SQLFileFilter implements FileFilter
{
	/* (non-Javadoc)
	 * @see java.io.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept( File aFile )
	{
		return ( aFile.isDirectory() || SQLParserUtil.isSQLFile( aFile ) );
	}
}