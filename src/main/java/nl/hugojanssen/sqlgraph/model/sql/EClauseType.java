package nl.hugojanssen.sqlgraph.model.sql;

/**
 * Lists the various roles of an SQL object within a SQL statement. I.e. in an UPDATE statement, the table that is
 * updated is the TARGET table and the table(s) that provides the data is/are the SOURCE table(s).
 * 
 * @author hjanssen
 */
public enum EClauseType
{
	/**
	 * The SQL object is a source object
	 */
	SOURCE,

	/**
	 * The SQL object is a target object
	 */
	TARGET,

	/**
	 * The role of the SQL object is unknown
	 */
	UNKNOWN
}
