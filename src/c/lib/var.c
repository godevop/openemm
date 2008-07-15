/*	-*- mode: c; mode: fold -*-	*/
/*********************************************************************************
 * The contents of this file are subject to the OpenEMM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.agnitas.org/openemm.
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied.  See the License for
 * the specific language governing rights and limitations under the License.
 *
 * The Original Code is OpenEMM.
 * The Initial Developer of the Original Code is AGNITAS AG. Portions created by
 * AGNITAS AG are Copyright (C) 2006 AGNITAS AG. All Rights Reserved.
 *
 * All copies of the Covered Code must include on each user interface screen,
 * visible to all users at all times
 *    (a) the OpenEMM logo in the upper left corner and
 *    (b) the OpenEMM copyright notice at the very bottom center
 * See full license, exhibit B for requirements.
 ********************************************************************************/
/** @file var.c
 * Handling for variable/value pairs.
 * This modules adds support for linked list of variable/value
 * pairs, memory handling and matching
 */
# include	<stdlib.h>
# include	<string.h>
# include	"agn.h"

/** Allocate a pair.
 * An instance for var_t is allocated and a copy of parameter
 * var and val is made (if not NULL) for the member variables
 * @param var the name of the variable
 * @param val the value
 * @return the new instance on success, NULL otherwise
 */
var_t *
var_alloc (const char *var, const char *val) /*{{{*/
{
	var_t	*v;
	
	if (v = (var_t *) malloc (sizeof (var_t))) {
		v -> var = NULL;
		v -> val = NULL;
		v -> next = NULL;
		if ((var && (! (v -> var = strdup (var)))) ||
		    (val && (! (v -> val = strdup (val)))))
			v = var_free (v);
	}
	return v;
}/*}}}*/
/** Frees a pair.
 * Returns the used memory of the instance to the system.
 * @param v the instance to use
 * @return NULL
 */
var_t *
var_free (var_t *v) /*{{{*/
{
	if (v) {
		if (v -> var)
			free (v -> var);
		if (v -> val)
			free (v -> val);
		free (v);
	}
	return NULL;
}/*}}}*/
/** Free the linked list.
 * Returns the used memory of the instance and all linked instances
 * to the system.
 * @param v the instance to start from
 * @return NULL
 * @see var_free
 */
var_t *
var_free_all (var_t *v) /*{{{*/
{
	var_t	*tmp;
	
	while (tmp = v) {
		v = v -> next;
		var_free (tmp);
	}
	return NULL;
}/*}}}*/
/** Set the variable name.
 * Sets/replaces the name of the variable.
 * @param v the instance to use
 * @param var the new name of the variable
 * @return true on success, false otherwise
 */
bool_t
var_variable (var_t *v, const char *var) /*{{{*/
{
	if (v -> var)
		free (v -> var);
	v -> var = var ? strdup (var) : NULL;
	return (! var) || v -> var ? true : false;
}/*}}}*/
/** Set the value.
 * Sets/replaces the value.
 * @param v the instance to use
 * @param val the new value
 * @return true on success, false otherwise
 */
bool_t
var_value (var_t *v, const char *val) /*{{{*/
{
	if (v -> val)
		free (v -> val);
	v -> val = val ? strdup (val) : NULL;
	return (! val) || v -> val ? true : false;
}/*}}}*/
static
# ifdef		__OPTIMIZE__
inline
# endif		/* __OPTIMIZE__ */
bool_t
do_match (var_t *v, const char *var, int (*func) (const char *, const char *)) /*{{{*/
{
	if (((! v -> var) && (! var)) ||
	    (v -> var && var && (! (*func) (v -> var, var))))
		return true;
	return false;
}/*}}}*/
/** Compare instance to variable.
 * Comapres the instance variable name to the given variable name
 * if the match
 * @param v the instance
 * @param var the variable name
 * @return true if they are equal, false otherwise
 */
bool_t
var_match (var_t *v, const char *var) /*{{{*/
{
	return do_match (v, var, strcmp);
}/*}}}*/
/** Compare instance to variable case insensitive.
 * Like <i>var_match</i> but ignores case
 * @param v the instance
 * @param var the variable name
 * @return true if they are equal ignoring case, false otherwise
 * @see var_match
 */
bool_t
var_imatch (var_t *v, const char *var) /*{{{*/
{
	return do_match (v, var, strcasecmp);
}/*}}}*/
static
# ifdef		__OPTIMIZE__
inline
# endif		/* __OPTIMIZE__ */
bool_t
do_part_match (var_t *v, const char *var, int (*func) (const char *, const char *, size_t)) /*{{{*/
{
	if ((! v -> var) && (! var))
		return true;
	if (v -> var && var) {
		int	v1 = strlen (v -> var),
			v2 = strlen (var);
		
		if ((v1 <= v2) && (! (*func) (v -> var, var, v1)))
			return true;
	}
	return false;
}/*}}}*/
/** Compare instance partial to variable.
 * Checks the instance variable name if it starts with the given
 * variable name prefix.
 * @param v the instance
 * @param var the variable name prefix
 * @return true if the instance variable name starts with var, false otherwise
 */
bool_t
var_partial_match (var_t *v, const char *var) /*{{{*/
{
	return do_part_match (v, var, strncmp);
}/*}}}*/
/** Compare instance partial to variable case insensitive.
 * Like <i>var_partial_match</i> but ignores case
 * @param v the instance
 * @param var the variable name prefix
 * @return true if the instance variable name starts with var ignoring case, false otherwise
 * @see var_partial_match
 */
bool_t
var_partial_imatch (var_t *v, const char *var) /*{{{*/
{
	return do_part_match (v, var, strncasecmp);
}/*}}}*/