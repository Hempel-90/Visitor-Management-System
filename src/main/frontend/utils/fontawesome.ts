
//region Imports--------------------------------------------------------------------------------------------------------

import { type IconDefinition, type IconProp, library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { fab } from '@fortawesome/free-brands-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

//endregion Imports-----------------------------------------------------------------------------------------------------


//region FontAwesome Setup----------------------------------------------------------------------------------------------

/*
 * Lädt alle benötigten Icon-Sets in die FontAwesome Library:
 *  - Solid Icons (fas)
 *  - Brand Icons (fab)
 *  - Regular Icons (far)
 */

library.add(fas as unknown as IconDefinition, fab as unknown as IconDefinition, far as unknown as IconDefinition);

//endregion FontAwesome Setup-------------------------------------------------------------------------------------------


//region Exports--------------------------------------------------------------------------------------------------------

export { FontAwesomeIcon, fas, fab, far };
export type { IconProp };

//endregion Exports-----------------------------------------------------------------------------------------------------
