<script>
import NodeService from "./services/NodeService";
import axios from 'axios';

export default {
  data() {
    return {
      fileInfoURL: "http://127.0.0.1:8080/get-chunk-info",
      resultURL: "http://127.0.0.1:8080/get-result",
      orgImageURL: "http://127.0.0.1:8080/get-original-image",
      procImageURL: "http://127.0.0.1:8080/get-processed-image",
      uploadURL: "http://127.0.0.1:8080/upload",
      progressURL: "http://127.0.0.1:8080/get-progress",
      defaultImageURL: "src/assets/default.png",
      smallSliderValue: 0,
      uploadFinished: false,
      calculationsFinished: false,
      bigSliderValue: 0,
      selectedKey: null,
      treeNodes: null,
      listNodes: null,
      selectedTreeNode: null,
      selectedListNode: {
        name: null,
        code: null
      },
      info: null,
      chunkNumber: null,
      orgBitCount: null,
      chunkSize: 4,
      bufferSize: 0,
      loading: false,
      parallelRows: 2,
      progress: {
        state: "start",
        value: 0
      },
      interval: null,
      result: {
        fn: "0",
        fp: "0",
        tp: "0",
        tn: "0",
      },
      ISBNSelection: null,
      orgImageKey: 0,
      procImageKey: 0,
      progressKey: 0,
    }
  },
  nodeService: null,
  created() {
    this.nodeService = new NodeService();
  },
  mounted() {
    this.nodeService.getTreeNodes().then(data => this.treeNodes = data)
    this.nodeService.getListNodes().then(data => this.listNodes = data)
  },
  methods: {
    reloadComponent(key) {
      return key += 1;
    },
    getImageURL(url) {
      return url + "?" + Math.random();
    },
    getBitCount() {
      if (this.uploadFinished === "true") {
        this.uploadFinished = false;
      }
      axios
          .get(this.fileInfoURL)
          .then(response => (
              this.orgBitCount = response.data,
                  this.uploadFinished = true,
                  this.orgImageKey += 1,
                  this.chunkSize = 4
          ))
          .catch(error => console.log(error));
      this.chunkNumber = 1;
      this.bufferSize = 0;
    },
    showSelected(node) {
      this.selectedTreeNode = node.data;
    },
    startProgress() {
      this.interval = setInterval(() => {
        axios.get(this.progressURL).then(
            (response) => {
              this.progress = response.data
              this.progressKey += 1
            }
        ), (error) => {
          console.log(error);
        }
      }, 700);
    },
    endProgress() {
      clearInterval(this.interval);
      this.interval = null;
    },
    getResult() {
      if (this.selectedTreeNode !== null) {
        this.loading = true;
        this.calculationsFinished = false;
        if (this.ISBNSelection == null && this.selectedTreeNode === 'ISBNCheck') {
          this.ISBNSelection = 9;
          this.chunkSize = 9;
        } else if (this.selectedTreeNode === 'ISBNCheck') {
          this.chunkSize = this.ISBNSelection;
        }
        this.startProgress();
        axios.post(
            this.resultURL, {
              redundancyMethodProvider: {
                parameters: [this.parallelRows, this.selectedListNode.code],
                method: this.selectedTreeNode
              },
              chunkInfo: {
                chunkSize: this.chunkSize,
                bufferSize: this.bufferSize,
              },
              noiseInfo: {
                noiseBytesPerMillion: this.bigSliderValue + this.smallSliderValue
              }
            }).then((response) => {
          this.result = response.data;
          this.calculationsFinished = true;
          this.endProgress();
          this.progress.value = 100;
          this.progress.state = "Done";
          this.loading = false;
        }, (error) => {
          console.log(error);
        });
      } else {
        alert("Choose coding method");
      }
    }
  }
}
</script>
<template>

  <Splitter style="height:98vh">
    <SplitterPanel :size="30" class="flex align-items-center justify-content-center">
      <Fieldset legend="Original image">
        <Splitter layout="vertical">
          <SplitterPanel :size="60" class="flex align-items-center justify-content-center">
            <Image v-if="uploadFinished===false" id="UploadDefaultImage" :src="defaultImageURL"
                   imageStyle="max-width:100%">
              <template #indicator>
                Preview Content
              </template>
            </Image>
            <Image v-if="uploadFinished" id="UploadedImage" :key="orgImageKey" :src="getImageURL(orgImageURL)"
                   imageStyle="max-width:100%" preview>
              <template #indicator>
                Preview Content
              </template>
            </Image>
          </SplitterPanel>
          <SplitterPanel :size="40" class="flex align-items-center justify-content-center">
            <FileUpload
                :auto="true"
                :max-file-size="1048576" :url="uploadURL" accept=".png"
                mode="basic" name="image" style="width:250px;" @upload="getBitCount()">
            </FileUpload>
          </SplitterPanel>
        </Splitter>
      </Fieldset>
    </SplitterPanel>
    <SplitterPanel :size="40">
      <TabView v-if="uploadFinished">
        <TabPanel>
          <template #header>
            <i class="pi pi-bolt"></i>
            <span>Error insertion</span>
          </template>
          <h2>Wybierz ilosc errorow w bitach na milion bitow</h2>
          <SplitterPanel :size="15" class="flex align-items-center justify-content-center">
            <Splitter>
              <SplitterPanel :size="50">
                <div style="padding: 10px">
                  <h3>Granular input </h3>
                  <InputText v-model.number="smallSliderValue" style="width: 100%;"></InputText>
                  <Slider v-model="smallSliderValue" :max="1000" :min="0" :step="1"/>
                </div>
              </SplitterPanel>
              <SplitterPanel :size="50">
                <div style="padding: 10px">
                  <h3>Bulk input </h3>
                  <InputText v-model.number="bigSliderValue" style="width: 100%;"></InputText>
                  <Slider v-model="bigSliderValue" :max="1000000-smallSliderValue" :min="0" :step="10000"/>
                </div>
              </SplitterPanel>
            </Splitter>
          </SplitterPanel>
          <SplitterPanel :size="8">
            <div style="float:left; ">
              <h2 v-if="bigSliderValue + smallSliderValue>9999">Total value: {{ bigSliderValue + smallSliderValue }}
                Total
                percentage:
                {{ Math.round((bigSliderValue + smallSliderValue) / 10000) }} %</h2>
              <h2 v-else>Total value: {{ bigSliderValue + smallSliderValue }} Total percentage:
                {{ (bigSliderValue + smallSliderValue) / 10000 }} %</h2>
            </div>
          </SplitterPanel>
        </TabPanel>
        <TabPanel>
          <template #header>
            <span>Method configuration</span>
            <i class="pi pi-calculator"></i>
          </template>
          <SplitterPanel :size="20">
            <div style="margin: 10px">
              <h2>Chunk Size</h2>
              <InputText v-model.number="chunkSize" style="width: 100%;"/>
              <Slider v-model="chunkSize" :max="Math.round(orgBitCount*0.1)" :min="4" :step="1"/>
              <h2>Additional buffer size </h2>
              <InputText v-model.number="bufferSize" style="width: 100%;"/>
              <Slider v-model="bufferSize" :max="chunkSize*3" :min="0" :step="1"/>
              <h2>Final Chunk Size {{ chunkSize + bufferSize }} bits</h2>
              <h2>Final Chunk Count {{ Math.ceil(orgBitCount / chunkSize) }}</h2>
            </div>
          </SplitterPanel>
          <SplitterPanel :size="57">
            <Tree v-model:selectionKeys="selectedKey" :value="treeNodes" selectionMode="single"
                  @node-select="showSelected"></Tree>
            <div v-show="selectedTreeNode === 'TwoDimParityCheck'" style="margin: 10px">
              <h2> Number of parallel chunks: {{ parallelRows }}</h2>
              <Slider v-model="parallelRows" :max="orgBitCount/chunkSize" :min="1" :step="1"/>
            </div>
            <div v-show="selectedTreeNode === 'CyclicRedundancyCheck'">
              <h2>Choose polynomial</h2>
              <Listbox v-model="selectedListNode" :options="listNodes" listStyle="max-height:150px" optionLabel="name"/>
            </div>
            <div v-show="selectedTreeNode === 'ISBNCheck'">
              <h2>Choose chunk Size</h2>
              <RadioButton v-model="ISBNSelection" name="ISBNChunkSize9" value="9"/>
              <label for="ISBNChunkSize1">9</label>
              <RadioButton v-model="ISBNSelection" name="ISBNChunkSize12" value="12"/>
              <label for="ISBNChunkSize2">12</label>
            </div>
          </SplitterPanel>
        </TabPanel>
      </TabView>
    </SplitterPanel>
    <SplitterPanel :size="30" class="flex align-items-center justify-content-center">
      <Fieldset v-if="uploadFinished" legend="Processed image">
        <Splitter layout="vertical">
          <SplitterPanel :size="60" class="flex align-items-center justify-content-center">
            <Image v-if="calculationsFinished===false" id="DownloadDefaultImage" :src="defaultImageURL"
                   imageStyle="max-width:100%">
              <template #indicator>
                Preview Content
              </template>
            </Image>
            <Image v-if="calculationsFinished" id="DownloadImage" :key="procImageKey" :src="getImageURL(procImageURL)"
                   imageStyle="max-width:100%" preview>
              <template #indicator>
                Preview Content
              </template>
            </Image>
          </SplitterPanel>
          <SplitterPanel :size="10">
            <ProgressBar :value="progress.value"/>
            <Button v-if="loading===true" :key="progressKey" :label=progress.state disabled="disabled"
                    style="width: 100%"/>
          </SplitterPanel>
          <SplitterPanel :size="10" class="flex align-items-center justify-content-center">
            <Button :loading="loading" icon="pi pi-download" label="Start Coding Process" style="width:250px;"
                    @click="getResult"></Button>
          </SplitterPanel>
          <SplitterPanel :size="20" class="flex align-items-center justify-content-center">
            <div v-if="calculationsFinished===true">
              <h2>Correctly detected {{ result.tp }} broken chunks</h2>
              <h2>Incorrectly detected {{ result.fp }} chunks</h2>
              <h2>Not detected {{ result.fn }} chunks</h2>
              <h2>Sensitivity {{ result.tp / (result.tp + result.fn) * 100 }}%</h2>
              <h2>Specificity {{ result.tn / (result.tn + result.fp) * 100 }}%</h2>
            </div>
          </SplitterPanel>
        </Splitter>
      </Fieldset>
    </SplitterPanel>
  </Splitter>

</template>