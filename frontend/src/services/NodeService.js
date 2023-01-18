export default class NodeService {
    getTreeNodes() {
        return fetch('src/assets/treenodes.json').then(res => res.json())
            .then(d => d.root);
    }
    getListNodes() {
        return fetch('src/assets/listnodes.json').then(res => res.json())
            .then(d => d.root);
    }
}