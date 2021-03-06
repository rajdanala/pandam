/*
Copyright (c) 2009-2014, Andrew M. Martin
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
conditions are met:

 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
   disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
   disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Pandam nor the names of its contributors may be used to endorse or promote products derived from this
   software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/
package org.pandcorps.pandax.text;

import org.pandcorps.pandam.*;
import org.pandcorps.pandax.in.*;

public abstract class MenuItem {
    protected final Panctor bound;
    protected ControlScheme ctrl = null;
    protected Panlayer layer = null;
    
    // Adding an item to a form will assign a ControlScheme, so it's not required.
    // Maybe bound should be part of ControlScheme.
    // If so, probably wouldn't be the same bound as MenuItem; each item in a form would have its own.
    protected MenuItem(final Panctor bound) {
        this(bound, null);
    }
    
    protected MenuItem(final Panctor bound, final ControlScheme ctrl) {
        this.bound = bound;
        this.ctrl = ctrl;
    }
    
    protected abstract void focus();
    
    protected void blur() {
        bound.unregisterListeners();
    }
    
    public final Panlayer getLayer() {
    	return layer;
    }
}
