/**
 *    
 *  Copyright [2013] [Aron Georgel - aron.georgel@gmail.com]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.georgelaron.lighttab;

import ro.georgelaron.R;

/**
 * Helper class for create animation when switching a fragment
 * @author Georgel Aron
 *
 */
public class LightAnimation {
    public enum Animation {
        LEFT_IN(R.animator.slide_left_in), LEFT_OUT(R.animator.slide_left_out), RIGHT_IN(R.animator.slide_right_in);

        private int animation;

        private Animation(int animation) {
            this.animation = animation;
        }

        public int getAnimation() {
            return this.animation;
        }
    }

    public static int getComplementaryAnimation(Animation anim) {
        switch (anim) {
        case LEFT_IN:
            return R.animator.slide_right_out;
        default:
            return R.animator.stay_still;
        }
    }

}
