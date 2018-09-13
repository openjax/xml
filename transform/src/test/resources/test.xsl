<!--
  Copyright (c) 2016 FastJAX
  
  Permission is hereby granted, free of charge, to any person obtaining a copy
  of this software and associated documentation files (the "Software"), to deal
  in the Software without restriction, including without limitation the rights
  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the Software is
  furnished to do so, subject to the following conditions:
  
  The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.
  
  You should have received a copy of The MIT License (MIT) along with this
  program. If not, see <http://opensource.org/licenses/MIT/>.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:template match="/hello-world">
    <HTML>
      <HEAD>
        <TITLE></TITLE>
      </HEAD>
      <BODY>
        <H1>
          <xsl:value-of select="greeting"/>
        </H1>
        <xsl:apply-templates select="greeter"/>
      </BODY>
    </HTML>
  </xsl:template>
  <xsl:template match="greeter">
    <DIV>from <I><xsl:value-of select="."/></I></DIV>
  </xsl:template>
</xsl:stylesheet>