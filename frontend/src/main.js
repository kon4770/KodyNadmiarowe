import { createApp } from 'vue'
import App from './App.vue'
import PrimeVue from "primevue/config";

import 'primevue/resources/themes/saga-blue/theme.css'       //theme
import 'primevue/resources/primevue.min.css'                 //core css
import 'primeicons/primeicons.css'                           //icons
import "primeflex/primeflex.css"                             //grid
import 'vue-image-zoomer/dist/style.css';

import Tree from 'primevue/tree';
import FileUpload from 'primevue/fileupload';
import Button from 'primevue/button';
import Slider from 'primevue/slider';
import Image from 'primevue/image';
import InputText from "primevue/inputtext";
import Splitter from "primevue/splitter";
import SplitterPanel from 'primevue/splitterpanel';
import Listbox from 'primevue/listbox';
import TabView from 'primevue/tabview';
import TabPanel from 'primevue/tabpanel';
import ProgressBar from 'primevue/progressbar';
import RadioButton from 'primevue/radiobutton';
import Fieldset from 'primevue/fieldset';


const app = createApp(App);

app.use(PrimeVue);

app.component('Tree', Tree);
app.component('Button', Button);
app.component('FileUpload', FileUpload);
app.component('Slider', Slider);
app.component('InputText', InputText);
app.component('Image', Image);
app.component('Splitter', Splitter);
app.component('SplitterPanel', SplitterPanel);
app.component('Listbox', Listbox);
app.component('TabView', TabView);
app.component('TabPanel', TabPanel);
app.component('ProgressBar', ProgressBar);
app.component('RadioButton', RadioButton);
app.component('Fieldset', Fieldset);
app.mount('#app');

