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
package org.pandcorps.furguardians;

import java.util.*;
import java.util.Map;

import org.pandcorps.core.*;
import org.pandcorps.core.seg.*;
import org.pandcorps.furguardians.Player.*;

public class Avatar extends EyeData implements Segmented {
	protected final static Map<String, Animal> SPECIAL_ANIMALS = new HashMap<String, Animal>();
	protected final static Map<String, BirdKind> BIRDS = new LinkedHashMap<String, BirdKind>();
	private final static BirdKind FIRST_BIRD;
	protected final static String FIRST_BIRD_NAME = "Egg";
	protected final static float DEF_JUMP_COL = 1;
	private final static int MAX_COLOR_INDEX = 4;
    protected String anm = null;
    protected final SimpleColor col = new SimpleColor();
    protected byte jumpMode = -1;
    protected final SimpleColor jumpCol = new SimpleColor();
    protected final Garb clothing = new Garb();
    protected final Garb hat = new Garb();
    protected final Dragon dragon = new Dragon();
    protected final SimpleColor col2 = new SimpleColor();
    protected final Bird bird = new Bird();
    
    private final static int[] randomColorChannels = {0, 1, 2};
    
    static {
        putSpecialAnimal(new Animal("Zebra", "Horse", 25000));
    	putSpecialAnimal(new Animal("Panda", "Bear", 50000));
    	FIRST_BIRD = new BirdKind("Robin", 30000);
    	putBird(FIRST_BIRD); // Expensive because this unlocks option to buy other birds
    	//putBird(new BirdKind("Sparrow", 1000));
    	putBird(new BirdKind("Blue Jay", 2000));
    	putBird(new BirdKind("Canary", 2000));
    	putBird(new BirdKind("Cardinal", 2000));
    	putBird(new BirdKind("Oriole", 3000));
    	putBird(new BirdKind("Parakeet", 3000));
    	putBird(new BirdKind("Purple Martin", 3000));
    	putBird(new BirdKind("Crow", 5000));
    	putBird(new BirdKind("Dove", 5000));
    	//Wyvern, colorable
    }
    
    private final static void putSpecialAnimal(final Animal animal) {
    	SPECIAL_ANIMALS.put(animal.getName(), animal);
    }
    
    private final static void putBird(final BirdKind bird) {
        BIRDS.put(bird.getName(), bird);
    }
    
    protected final static class Garb implements Named {
    	protected Clothing clth = null;
    	protected final SimpleColor col = new SimpleColor();
    	
    	protected final void init() {
    		clth = null;
    		col.init();
    	}
    	
    	protected final void load(final Garb garb) {
    		clth = garb.clth;
    		col.load(garb.col);
    	}
    	
    	protected final void save(final Segment seg, final int i) {
    		seg.setValue(i, (clth == null) ? "" : clth.res);
        	col.save(seg, i + 1);
    	}

        @Override
        public final String getName() {
            return Player.getName(clth);
        }
    }
    
    protected final static class SimpleColor {
    	protected float r = -1; // These should probably be multiples of 0.25
        protected float g = -1;
        protected float b = -1;
        
        protected final void init() {
        	r = g = b = DEF_JUMP_COL;
        }
        
        protected final void set(final float r, final float g, final float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
        
        protected final void load(final SimpleColor col) {
        	set(col.r, col.g, col.b);
        }
        
        protected final void load(final Segment seg, final int i) {
        	set(seg.getFloat(i, DEF_JUMP_COL), seg.getFloat(i + 1, DEF_JUMP_COL), seg.getFloat(i + 2, DEF_JUMP_COL));
        }
        
        protected final void save(final Segment seg, final int i) {
        	seg.setFloat(i, r);
        	seg.setFloat(i + 1, g);
        	seg.setFloat(i + 2, b);
        }
        
        protected final void randomize() {
        	do {
                set(randColor(), randColor(), randColor());
            } while (r == 0 && g == 0 && b == 0);
        }
        
        protected final void randomizeColorful() {
        	final int max = Mathtil.randi(MAX_COLOR_INDEX - 1, MAX_COLOR_INDEX);
        	final int min = Mathtil.randi(0, 1);
        	final int mid = Mathtil.randi(min, max);
        	Mathtil.shuffle(randomColorChannels);
        	setIndex(randomColorChannels[0], max);
        	setIndex(randomColorChannels[1], mid);
        	setIndex(randomColorChannels[2], min);
        }
        
        protected final void randomizeColorfulDifferent(final SimpleColor ref) {
        	do {
        		randomizeColorful();
        	} while (getDistance(ref) < 6);
        }
        
        protected final void negate() {
        	set(1f - r, 1f - g, 1f - b);
        }
        
        protected final int getDistance(final SimpleColor c) {
        	int d = 0;
        	for (int i = 0; i < 3; i++) {
        		final int dif = getIndex(i) - c.getIndex(i);
        		d += (dif * dif);
        	}
        	return d;
        }
        
        protected final int getIndex(final int channel) {
        	final float color;
        	if (channel == 0) {
        		color = r;
        	} else if (channel == 1) {
        		color = g;
        	} else {
        		color = b;
        	}
        	return toIndex(color);
        }
        
        protected final void setIndex(final int channel, final int i) {
        	final float color = toColor(i);
        	if (channel == 0) {
        		r = color;
        	} else if (channel == 1) {
        		g = color;
        	} else {
        		b = color;
        	}
        }
    }
    
    protected static class Animal extends FinName {
    	protected final String base;
    	private final int cost;
    	
		protected Animal(final String name, final String base, final int cost) {
			super(name);
			this.base = base;
			this.cost = cost;
		}
		
		public final int getCost() {
            return cost;
        }
    }
    
    protected static class BirdKind extends Animal {
        protected BirdKind(final String name, final int cost) {
            super(name, null, cost);
        }
    }
    
    protected static class Dragon extends EyeData {
    	protected final SimpleColor col = new SimpleColor();
        
        protected Dragon() {
        	setName("Dragon");
        }
        
        protected final void init() {
        	col.init();
        }
    }
    
    protected static class Bird extends EyeData {
    	protected String kind = null;
    	
        protected Bird() {
        	setName("Bird");
        }
    }
    
    protected static class Clothing extends FinName {
        protected final String res;
        private final int cost;
        private final String body;
        protected Img[] imgs = null;
        protected Img[] mapImgs = null;
        protected Img[] rideImgs = null;
        
        protected Clothing(final String name, final String res, final int cost) {
            this(name, res, cost, null);
        }
        
        protected Clothing(final String name, final String res, final int cost, final String body) {
            super(name);
            this.res = res;
            this.cost = cost;
            this.body = body;
        }
        
        protected String getLoc() {
        	return "clothes";
        }
        
        protected int getDim() {
        	return 32;
        }
        
        public final void init() {
            if (imgs != null) {
                return;
            }
            final String loc = getLoc();
            final int d = getDim();
            imgs = FurGuardiansGame.loadChrStrip(loc + "/" + res + ".png", d, true);
            if (!res.equals("RoyalMantle")) {
            	mapImgs = FurGuardiansGame.loadChrStrip(loc + "/" + res + "Map.png", d, true);
            	if (getClass() == Clothing.class) {
            		rideImgs = FurGuardiansGame.loadChrStrip(loc + "/" + res + "Ride.png", d, true);
            	}
            }
            Img.setTemporary(false, imgs);
            Img.setTemporary(false, mapImgs);
            Img.setTemporary(false, rideImgs);
        }
        
        public final int getCost() {
            return cost;
        }
        
        public final String getBody() {
            return body;
        }
    }
    
    protected final static int INDEX_ROYAL_ROBE = 5;
    protected final static int INDEX_ROYAL_DRESS = 6;
    
    protected final static Clothing[] clothings = {
        new Clothing("Sleeveless", "AShirt", 1000),
        new Clothing("Short Sleeves", "TShirt", 1500),
        new Clothing("Long Sleeves", "LongShirt", 2000),
        new Clothing("Dress", "Dress", 2500),
        new Clothing("Armor", "Armor", 50000, "Tough"),
        new Clothing("Royal Robe", "RoyalRobe", 100000), // Change INDEX_ROYAL_ROBE if adding clothes above here
        new Clothing("Royal Dress", "RoyalDress", 100000)
    };
    
    protected final static Clothing[] hiddenClothings = {
        new Clothing("Royal Mantle", "RoyalMantle", 100000),
        clothings[INDEX_ROYAL_ROBE]
    };
    
    protected final static Clothing getClothing(final String name) {
        for (final Clothing c : clothings) {
            if (c.res.equals(name)) {
                return c;
            }
        }
        for (final Clothing c : hiddenClothings) {
            if (c.res.equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    protected static class Hat extends Clothing {
    	protected final boolean maskNeeded;
    	protected final boolean backNeeded;
    	
        protected Hat(final String name, final String res, final int cost) {
        	this(name, res, cost, true);
        }
        
        protected Hat(final String name, final String res, final int cost, final boolean maskNeeded) {
        	this(name, res, cost, maskNeeded, true);
        }
        
        protected Hat(final String name, final String res, final int cost, final boolean maskNeeded, final boolean backNeeded) {
            super(name, res, cost);
            this.maskNeeded = maskNeeded;
            this.backNeeded = backNeeded;
        }
        
        @Override
        protected final String getLoc() {
        	return "headgear";
        }
        
        @Override
        protected final int getDim() {
        	return 18;
        }
    }
    
    protected final static String HAT_CROWN = "Crown";
    
    protected final static Hat[] hats = {
        new Hat("Headband", "Headband", 1000),
        new Hat("Bandana", "Bandana", 1500),
        new Hat("Cap", "Cap", 2000),
        new Hat("Bow", "Bow", 2500, false, false),
        new Hat("Glasses", "Glasses", 3000, false),
        new Hat("Helm", "Helm", 50000, false),
        new Hat(HAT_CROWN, "Crown", 100000, false)
    };
    
    protected final static Hat getHat(final String name) {
        for (final Hat c : hats) {
            if (c.res.equals(name)) {
                return c;
            }
        }
        return null;
    }
    
    public Avatar() {
    }
    
    public Avatar(final Avatar src) {
        load(src);
    }
    
    public void randomize() {
        anm = Mathtil.rand(FurGuardiansGame.getBasicAnimals());
        //eye = Mathtil.randi(1, FurGuardiansGame.getNumEyes());
        eye = Mathtil.randElemI(org.pandcorps.furguardians.Map.EYES_BASIC);
        col.randomize();
        jumpMode = Player.MODE_NORMAL;
        jumpCol.init();
        clothing.init();
        hat.init();
        dragon.init();
        dragon.eye = Mathtil.randi(1, FurGuardiansGame.getNumDragonEyes());
        col2.set(1, 1, 1);
    }
    
    public void load(final Avatar src) {
        setName(src.getName());
        anm = src.anm;
        eye = src.eye;
        col.load(src.col);
        jumpMode = src.jumpMode;
        jumpCol.load(src.jumpCol);
        clothing.load(src.clothing);
        hat.load(src.hat);
        dragon.col.load(src.dragon.col);
        dragon.setName(src.dragon.getName());
        dragon.eye = src.dragon.eye;
        col2.load(src.col2);
        bird.setName(src.bird.getName());
        bird.eye = src.bird.eye;
        bird.kind = src.bird.kind;
    }
    
    public void load(final Segment seg) {
    	setName(seg.getValue(0));
    	anm = seg.getValue(1);
    	eye = seg.intValue(2);
    	col.load(seg, 3);
    	jumpMode = seg.getByte(6, Player.MODE_NORMAL);
    	jumpCol.load(seg, 7); // 7-9
    	clothing.clth = getClothing(seg.getValue(10));
    	clothing.col.load(seg, 11); // 11-13
    	hat.clth = getHat(seg.getValue(14));
    	hat.col.load(seg, 15); // 15-17
    	dragon.col.load(seg, 18); // 18-20
    	dragon.setName(seg.getValue(21, "Dragon"));
    	dragon.eye = seg.getInt(22, 1);
    	if (dragon.eye < 1) {
    		dragon.eye = 1;
    	}
    	col2.load(seg, 23); // 23-25
    	bird.setName(seg.getValue(26, "Bird"));
    	bird.eye = seg.getInt(27, 1);
    	bird.kind = seg.getValue(28);
    }
    
    @Override
    public void save(final Segment seg) {
        seg.setName(FurGuardiansGame.SEG_AVT);
    	seg.setValue(0, getName());
    	seg.setValue(1, anm);
    	seg.setInt(2, eye);
    	col.save(seg, 3);
    	seg.setInt(6, jumpMode);
    	jumpCol.save(seg, 7);
    	clothing.save(seg, 10); // 10 - 13
    	hat.save(seg, 14); // 14 - 17
    	dragon.col.save(seg, 18);
    	seg.setValue(21, dragon.getName());
    	seg.setInt(22, dragon.eye);
    	col2.save(seg, 23); // 23-25
    	seg.setValue(26, bird.getName());
    	seg.setInt(27, bird.eye);
    	seg.setValue(28, bird.kind);
    }
    
    protected final Animal getAnimal() {
    	return getSpecialAnimal(anm);
    }
    
    protected final static Animal getSpecialAnimal(final String anm) {
        return SPECIAL_ANIMALS.get(anm);
    }
    
    protected final String getBaseAnm() {
    	final Animal animal = getAnimal();
    	return animal == null ? anm : animal.base;
    }
    
    protected final static BirdKind getBird(final String brd) {
        final BirdKind kind = BIRDS.get(brd);
        if (kind != null) {
            return kind;
        }
        return FIRST_BIRD_NAME.equals(brd) ? FIRST_BIRD : null;
    }
    
    private final static float randColor() {
        return toColor(Mathtil.randi(0, MAX_COLOR_INDEX));
    }
    
    protected final static float toColor(final int i) {
        return i / 4f; // MAX_COLOR_INDEX
    }
    
    private final static int toIndex(final float c) {
    	return Math.round(c * MAX_COLOR_INDEX);
    }
}
