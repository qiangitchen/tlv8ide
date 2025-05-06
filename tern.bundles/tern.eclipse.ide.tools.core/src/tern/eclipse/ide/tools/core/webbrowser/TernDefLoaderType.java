package tern.eclipse.ide.tools.core.webbrowser;

/**
 * JSON Type definition are json file. To load JSON, there is 3 means :
 * 
 * <ul>
 * <li>load JSON with Ajax</li>
 * <li>embed JSON in the HTML page of the editor.</li>
 * <li>embed JSON in a JS file.</li>
 * </ul>
 */
public enum TernDefLoaderType {

	LoadDefWithAjax, EmbedDefInHTML, EmbedDefInJS
}
