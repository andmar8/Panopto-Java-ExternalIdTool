Panopto-Java-ExternalIdTool
===========================

A tool for making dealing with external ids, panopto ids and entity names somewhat easier. This tool is not exhaustively implemented and for the most part has quite a few rough edges, but was only really intended to do a couple of gets and sets although potentially could be much more useful! We still use this tool and find it very useful indeed, especially in the development phase.

One other aspect to this tool that is quite important to know, Panopto implemented external Id's in API version 4.2 but were not convinced it would be a widely used feature, so you can **ONLY** access external Id's using the API, since that means there is **NOTHING** in the Panopto GUI for accessing external Id's and only implicit ways of getting at Panopto Id's*. This tool allows much easier access to names, Id's and external Id's and is the **ONLY** **interactive** way to **set** external Id's short of writing your own code to explicitly retrieve a given external Id.... which would be a waste of you time now you've found this code.

The tool covers Id's, Names and External Id's for folders, groups, remote recorders and sessions, just to reiterate, some sections are more mature and less bugged than others, in essence this is still a "sort of work in progress" but is feature complete enough for our purposes.

For more information on why we're using (and need) external Id's see [here](https://github.com/andmar8/Panopto-PHP-Booking-Engine#what-are-external-ids)

\* For example, how do I get a folder's Id or user's Id??? Not their name, the internally designated Id that Panopto uses in it's own API? You have to get it from URL's and other round about manners using the GUI :(

Libraries required to compile
-----------------------------

* Apache AXIS... we used version 1.6.2
* [Panopto-Java-Util](https://github.com/andmar8/Panopto-Java-Util) Library

How to use the jar
------------------

<pre>
java -jar PanoptoExternalIdTool &lt;Server&gt; &lt;Username&gt; &lt;Password&gt; &lt;Operation&gt; &lt;Type&gt; &lt;By&gt; &lt;Name|Id|ExternalId&gt; [Desired ExternalId(set only)]"
</pre>

Here are some examples...

<pre>

The general idea is getting works like this...
get	&lt;folder|recorder|session&gt;	&lt;name|id|exid&gt;	&lt;name|id|exid&gt;

...setting external Id's (the only thing you can set) works like this...
setExId	&lt;folder|recorder|session name|id&gt;		&lt;name|id&gt;		&lt;exid&gt;

...so to set an external id on a folder which you are querying by name, do the following...
java -jar PanoptoExternalIdTool panoptoserver.example.com admin password setExId folder name MAS1342 Q1213-MAS1342

..or say you just wanted to get a folder's details by name...
java -jar PanoptoExternalIdTool panoptoserver.example.com admin password get folder name MAS1342

..or by external id
java -jar PanoptoExternalIdTool panoptoserver.example.com admin password get folder exid Q1213-MAS1342

</pre>

Slight nuance
-------------

There is an oversight in the Panopto API (which I think is fixed in later builds) whereby you can not return the external Id details on folders (...I'll have to double check this), you can only "search" for them, so to see if an external id is set you'd have to set it then do a get to see if it's retrieved.