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
* fmt.java
* @author Sidharth Mishra
* @created Tue Oct 30 2018 22:08:50 GMT-0700 (PDT)
* @last-modified Wed Oct 31 2018 00:37:14 GMT-0700 (PDT)
*/

package io.github.sidmishraw.fmt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * fmt is an utility for generating parameteric messages. For example:
 * {@code fmt.inTemplate("{key} is having {value}").put("key",
 * "King").put("value", "Queen").eval()}
 * 
 * @author Sidharth Mishra <sidmishraw@gmail.com>
 * @version 1.0.0
 * @since 1.0.0
 */
public class fmt {

    private static Lock lock = new ReentrantLock();

    private static final fmt getInstance() {
        return new fmt();
    }

    private String template;

    private Map<String, String> paramMap;

    private fmt() {
        template = "";
        paramMap = new HashMap<>();
    }

    /**
     * A fluent API, creates the formatter instance by setting the template text.
     * 
     * @param template the template string
     * @return the formatter instance
     */
    public static final fmt inTemplate(String template) {
        var fmt = getInstance();
        fmt.template = template;
        return fmt;
    }

    /**
     * Puts the template param key and param values into the fmt.
     * 
     * @param templateParamKey   the template parameter key, the key needs to be a
     *                           valid identifier value
     * 
     * @param templateParamValue the template para eter value
     * @return the updated formatter instance
     */
    public final fmt put(String templateParamKey, String templateParamValue) {
        this.paramMap.put(templateParamKey, templateParamValue);
        return this;
    }

    /**
     * The terminal operation of the formatter instance. It evaluates the template
     * by substituting their values.
     * 
     * @return the final result of the template with all its values substituted.
     */

    public final String eval() {
        if (this.paramMap.isEmpty()) {
            // since no parameter values were provided, return the template as is
            return this.template;
        }
        return this.resolveTemplate(this.template, this.paramMap);
    }

    private String resolveTemplate(String template, Map<String, String> params) {
        if (Objects.isNull(template) || template.isBlank() || template.isEmpty()) {
            return template;
        }
        var t = template;
        // resolve the template once
        for (var param : params.entrySet()) {
            final var paramKeyRegex = "\\{" + param.getKey() + "\\}";
            t = recEval(t, paramKeyRegex, param.getValue());
        }
        // if the template can no longer be resolved, it will remain unchanged
        // hence we can bail out...
        if (t.equals(template)) {
            return t;
        }
        // try resolving recursively
        return resolveTemplate(t, params);
    }

    private String recEval(String template, String paramKeyRegex, String paramValue) {
        var modifiedTemplate = Optional.ofNullable(template).map(t -> t.replaceAll(paramKeyRegex, paramValue))
                .orElse(template);
        if (Optional.ofNullable(template).filter(t -> t.equals(modifiedTemplate)).isPresent()) {
            return template;
        }
        return recEval(modifiedTemplate, paramKeyRegex, paramValue);
    }
}
