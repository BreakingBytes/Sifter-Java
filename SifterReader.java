//package com.SifterReader;

/*
 *      SifterReader.java
 *      
 *      Copyright 2012 Mark Mikofski <marko@linuxBox>
 *      
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *      
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *      
 *      You should have received a copy of the GNU General Public License
 *      along with this program; if not, write to the Free Software
 *      Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 *      MA 02110-1301, USA.
 *      
 *      
 */

import java.net.*;
import java.io.*;
import org.json.*;

/** SifterAPI Reader using org.json library **/

public class SifterReader {

    public static void main (String[] args) throws Exception {
        /** Enter your sifter account and access key in the 
		 ** string fields below */
		
        // create URL object to SifterAPI
		String yourAccount = "your-account-name"; // enter your account name
		String yourDomain = "yourAccount" + ".sifterapp.com";
        URL sifter = new URL("https",yourDomain,"/api/projects");
        
        // open connectino to SifterAPI
        URLConnection sifterConnection = sifter.openConnection();
        
        // add Access Key to header request
		String yourAccessKey = "your-sifterapi-32char-access-key"; // enter your access key
        sifterConnection.setRequestProperty("X-Sifter-Token",
			yourAccessKey);
		
		// add Accept: application/json to header - also not necessary
		sifterConnection.addRequestProperty("Accept","application/json");
		
		// URLconnection.connect() not necessary
		// getInputStream will connect
        
        // don't make a content handler, org.json reads a stream!
        
        // create buffer and open input stream
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                sifterConnection.getInputStream()));
        // don't readline, create stringbuilder, append, create string
        
        // construct json tokener from input stream or buffered reader
        JSONTokener x = new JSONTokener(in);
        
        // initialize "projects" JSONObject from string
        JSONObject projects = new JSONObject(x);
        
        // prettyprint "projects"
        System.out.println("************ projects ************");
        System.out.println(projects.toString(2));
        
        // array of projects
        JSONArray array = projects.getJSONArray("projects");
        int arrayLength = array.length();
        JSONObject[] p = new JSONObject[arrayLength];
        
        // projects
        for (int i=0;i<arrayLength;i++) {
			p[i] = array.getJSONObject(i);
			System.out.println("************ project: " + (i+1) + " ************");
			System.out.println(p[i].toString(2));
        }
        
        // check field names
        String[] checkNames = {
			"api_url",
			"archived",
			"api_issues_url",
			"milestones_url",
			"api_milestones_url",
			"api_categories_url",
			"issues_url",
			"name",
			"url",
			"api_people_url",
			"primary_company_name"};
		
		// project field names
		String[] fieldNames = JSONObject.getNames(p[0]);
		int numKeys = p[0].length();
		SifterProj[] proj = new SifterProj[arrayLength];
		
		for (int j=0;j<arrayLength;j++) {
			proj[j] = new SifterProj(p[j].get("api_url").toString(),
						p[j].get("archived").toString(),
						p[j].get("api_issues_url").toString(),
						p[j].get("milestones_url").toString(),
						p[j].get("api_milestones_url").toString(),
						p[j].get("api_categories_url").toString(),
						p[j].get("issues_url").toString(),
						p[j].get("name").toString(),
						p[j].get("url").toString(),
						p[j].get("api_people_url").toString(),
						p[j].get("primary_company_name").toString());
			System.out.println("************ project: " + (j+1) + " ************");
			for (int i=0;i<numKeys;i++) {
				System.out.print(fieldNames[i]);
				System.out.print(" : ");
				System.out.println(p[j].get(fieldNames[i]));
			}
		}
	}
}
