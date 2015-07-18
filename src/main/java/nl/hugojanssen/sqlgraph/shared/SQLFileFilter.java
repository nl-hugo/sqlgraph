package nl.hugojanssen.sqlgraph.shared;

import java.io.File;
import java.io.FileFilter;

public class SQLFileFilter implements FileFilter
{
	@Override
	public boolean accept( File aFile )
	{
		return ( aFile.isDirectory() || SQLParserUtil.isSQLFile( aFile ) );
	}
}