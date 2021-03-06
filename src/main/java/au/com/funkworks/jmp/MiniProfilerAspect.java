/**
 * Copyright (C) 2011 by Alvin Singh
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package au.com.funkworks.jmp;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MiniProfilerAspect {

	@Pointcut(value="execution(public * *(..))")
	public void anyPublicMethod() {
	}
	
	@Around("anyPublicMethod() && @annotation(profiler)")
	public Object profiler(ProceedingJoinPoint pjp, Profiler profiler) throws Throwable {
		String desc = profiler.value();
		if (desc == null || desc.equals("")) {
			desc = pjp.getSignature().toShortString();
			
			if (desc.endsWith("()")) {
				desc = desc.substring(0, desc.length()-2);
			} else if (desc.endsWith("(..)")) {
				desc = desc.substring(0, desc.length()-4);
			}
		}
		Step step = MiniProfiler.step(desc);
		try {
			return pjp.proceed();
		} finally {
		  step.close();
		}		
	}
	
}