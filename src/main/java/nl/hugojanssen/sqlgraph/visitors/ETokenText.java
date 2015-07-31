package nl.hugojanssen.sqlgraph.visitors;

/**
 * Lists SQL tokens
 * 
 * @author hjanssen
 */
public enum ETokenText
{
	/**
	 * Token to create a temporary table; e.g. CREATE TEMPORARY TABLE tableXYZ AS ...
	 */
	TEMPORARY,

	/**
	 * Token to create a temporary table; e.g. CREATE TEMP TABLE tableXYZ AS ...
	 */
	TEMP
}
