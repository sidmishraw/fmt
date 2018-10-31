/*
*  BSD 3-Clause License
* 
* Copyright (c) 2018, Sidharth Mishra
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* 
* * Redistributions of source code must retain the above copyright notice, this
*   list of conditions and the following disclaimer.
* 
* * Redistributions in binary form must reproduce the above copyright notice,
*   this list of conditions and the following disclaimer in the documentation
*   and/or other materials provided with the distribution.
* 
* * Neither the name of the copyright holder nor the names of its
*   contributors may be used to endorse or promote products derived from
*   this software without specific prior written permission.
* 
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*
* fmtTest.java
* @author Sidharth Mishra
* @created Tue Oct 30 2018 22:08:50 GMT-0700 (PDT)
* @last-modified Wed Oct 31 2018 00:37:50 GMT-0700 (PDT)
*/

package io.github.sidmishraw.fmt;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for fmt.
 */
public class fmtTest {

    @org.junit.Test
    public void test_blank_template() {
        String template = null;
        var result = fmt.inTemplate(template).eval();
        System.out.println(result);
        Assert.assertNull(result);
    }

    @org.junit.Test
    public void test_blank_params() {
        final var template = "{king} will rock your world {{king}}";
        var result = fmt.inTemplate(template).eval();
        System.out.printf("t = %s, a = %s \n", template, result);
        Assert.assertEquals(template, result);
    }

    @org.junit.Test
    public void test_actual_params() {
        final var template = "{king} will rock your world {{king}}";
        var result = fmt.inTemplate(template).put("king", "kong").put("kong", "king").eval();
        System.out.printf("t = %s, a = %s \n", template, result);
        Assert.assertEquals("kong will rock your world king", result);
    }

    @org.junit.Test
    public void test_escape_braces() {
        final var template = "\\{king\\} is not {king}";
        var result = fmt.inTemplate(template).put("king", "kong").eval();
        System.out.printf("t = %s, a = %s \n", template, result);
        Assert.assertEquals("\\{king\\} is not kong", result);
    }

    @org.junit.Test
    public void test_integral_template_params() {
        final var template = "{0} is not 0, but {1} is 1?";
        var result = fmt.inTemplate(template).put("0", "Zero").put("1", "One").eval();
        System.out.printf("t = %s, a = %s \n", template, result);
        Assert.assertEquals("Zero is not 0, but One is 1?", result);
    }

    @org.junit.Test
    public void test_integral_template_params2() {
        final var template = "{0} is not 0, but {{1}} is 1?";
        var result = fmt.inTemplate(template).put("One", "11").put("0", "Zero").put("1", "One").eval();
        System.out.printf("t = %s, a = %s \n", template, result);
        Assert.assertEquals("Zero is not 0, but 11 is 1?", result);
    }

    @org.junit.Test
    public void test_integral_template_params_kong_king_problem() {
        final var template = "{king}{{one}}{{{kong}}}";
        var result = fmt.inTemplate(template).put("king", "kong").put("one", "two").put("kong", "king").eval();
        System.out.printf("t = %s, a = %s \n", template, result);
    }
}
