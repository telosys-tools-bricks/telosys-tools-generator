/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.generator.context.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Laurent GUERIN
 * 
 */
public final class Pluralizer {
	
    private static final Set<String> invariants = new HashSet<>();
    private static final Map<String, String> irregulars = new HashMap<>();
    static {
        invariants.addAll(Arrays.asList("sheep", "fish", "deer", "series", "species", "aircraft"));
        
        irregulars.put("child", "children");
        irregulars.put("person", "people");
        irregulars.put("man", "men");
        irregulars.put("woman", "women");
        irregulars.put("mouse", "mice");
        irregulars.put("goose", "geese");
        irregulars.put("tooth", "teeth");
        irregulars.put("foot", "feet");
        irregulars.put("ox", "oxen");
        irregulars.put("cactus", "cacti");
        irregulars.put("fungus", "fungi");
        irregulars.put("nucleus", "nuclei");
        irregulars.put("syllabus", "syllabi");
        irregulars.put("analysis", "analyses");
        irregulars.put("crisis", "crises");
        irregulars.put("thesis", "theses");
        irregulars.put("phenomenon", "phenomena");
        irregulars.put("criterion", "criteria");
        irregulars.put("datum", "data");
        irregulars.put("appendix", "appendices");
        irregulars.put("index", "indices");
        irregulars.put("matrix", "matrices");
        irregulars.put("vertex", "vertices");
        irregulars.put("axis", "axes");
        irregulars.put("radius", "radii");
        irregulars.put("medium", "media");
        irregulars.put("bacterium", "bacteria");
        irregulars.put("curriculum", "curricula");
        irregulars.put("stimulus", "stimuli");
        irregulars.put("alumnus", "alumni");
        irregulars.put("genus", "genera");
    }
    
    private boolean isVowel(char c) {
        return "aeiou".indexOf(Character.toLowerCase(c)) != -1;
    }
    private boolean endsWithConsonantAndY(String word) {
    	// If a noun ends in a consonant and followed by a ‘y’ (eg: army, story, berry)
    	char beforeLast = word.charAt(word.length() - 2);
    	return word.endsWith("y") && word.length() > 1 && !isVowel(beforeLast) ;
    }
    private boolean endsWithConsonantAndO(String word) {
    	// If a noun ends in a consonant and followed by a ‘o’ (eg: hero, volcano, echo )
    	char beforeLast = word.charAt(word.length() - 2);
    	return word.endsWith("o") && word.length() > 1 && !isVowel(beforeLast) ;
    }
    
    protected String pluralizeLowerCaseNoun(String noun) {
        if (noun == null || noun.isEmpty()) return noun;
        // Irregular
        if (irregulars.containsKey(noun)) {
            return irregulars.get(noun);
        }
        // Invariant
        else if (invariants.contains(noun)) {
            return noun;
        }
        // Regular rules
        else if (endsWithConsonantAndY(noun)) {
        	// If a noun ends in a consonant and followed by a ‘y’, then the ‘y’ is removed and ‘ies’ is added.
        	// army  -> armies
        	// story -> stories
        	// berry -> berries
            return noun.substring(0, noun.length() - 1) + "ies";
        } 
        else if (noun.endsWith("s") || noun.endsWith("x") || noun.endsWith("z") || noun.endsWith("ch") || noun.endsWith("sh") ) {
        	// If a noun ends in an ‘s’ , ‘sh’, ‘ch’, ‘x’ or 'z' => add "es" on the end of the word to make it plural.
        	// bus -> buses,  box -> boxes,  church -> churches,  dish -> dishes
            return noun + "es";
        } 
        else if (endsWithConsonantAndO(noun)) {
        	// If a noun ends in a consonant followed by an ‘o’ => also add "es" to the end of the word 
        	// hero  -> heroes
        	// volcano -> volcanoes
        	// echo  -> echoes
            return noun + "es";
        } 
        else {
        	// default : just add "s"
            return noun + "s";
        }
    }

    public String pluralize(String noun) {
        if (noun == null || noun.isEmpty()) return noun;
        String plural = pluralizeLowerCaseNoun( noun.toLowerCase());
        return restoreUpperCase(noun, plural);
    }
    protected String restoreUpperCase(String s1, String s2) {
        if (s1 == null || s2 == null) return s2;
    	char[] chars = s2.toCharArray();
        for (int i = 0 ; i < s1.length() ; i++) { 
        	char c1 = s1.charAt(i);
        	if ( i < s2.length() ) {
            	char c2 = s2.charAt(i);
                if ( c1 == Character.toUpperCase(c2) ) {
                	chars[i] = c1;  // same char but uppercase => replace by c1
                }
                // else : keep c2
        	}
        	// s2 is shorter than s1 => ignore c1
        }
        return new String(chars);
    }
}