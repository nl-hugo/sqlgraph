package nl.hugojanssen.sqlgraph.visitors;

import gudusoft.gsqlparser.ETokenType;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.TSourceTokenList;

/**
 * Provides utility functions for the SQLParser and Visitors.
 * 
 * @author hjanssen
 */
public class TVisitorUtil
{
	/**
	 * Checks if the token list containsa token to create a temporary table.
	 * 
	 * @param list the list to check
	 * @return true if the list contains a token that creates a temporary table, false otherwise
	 */
	static boolean containsTempToken( TSourceTokenList list )
	{
		boolean result = false;
		if ( list != null )
		{
			while ( !result && list.hasNext() )
			{
				TSourceToken token = list.next();
				result = isTempToken( token );
			}
		}
		return result;
	}

	/**
	 * Checks if the token is a token to create a temporary table.
	 * 
	 * @param token the token to check
	 * @return true if the token creates a temporary table, false otherwise
	 */
	static boolean isTempToken( TSourceToken token )
	{
		boolean result = false;
		if ( token != null )
		{
			result =
				token.issolidtoken()
					&& token.tokentype.equals( ETokenType.ttkeyword )
					&& ( token.astext.equalsIgnoreCase( ETokenText.TEMPORARY.name() ) || token.astext.equalsIgnoreCase( ETokenText.TEMP.name() ) );
		}
		return result;
	}

	// Constructor must be private to prevent creating new object
	private TVisitorUtil()
	{
	}
}