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

import android.util.Log;

public class LightTabException extends Exception {
    private static final long serialVersionUID = -6447563537451478727L;
    private static final String TAG = "[LIGHT TAB]";

    public LightTabException(String message) {
        Log.d(TAG, message);
    }

    public LightTabException(String message, LoggerLevel logLevel) {
        switch (logLevel) {
        case VERBOSE:
            Log.v(TAG, message);
            break;
        case ERROR:
            Log.e(TAG, message);
            break;
        case WARN:
            Log.w(TAG, message);
            break;
        case INFO:
            Log.i(TAG, message);
            break;
        case DEBUG:
        default:
            Log.d(TAG, message);
            break;
        }
    }
}
