import json
from flask import Flask, request, jsonify
from explanationqueries.causal_path_identification import run_select_query, get_state_to_explain
import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

def load_config(filename):
    with open(filename, 'r') as f:
        config = json.load(f)
    return config

app = Flask(__name__)

@app.route('/explanations', methods=['GET'])
def get_explanations():
    datetime_str = request.args.get('datetime')
    logger.info(f"GET Request /explanations ?datetime={datetime_str}")
    if not datetime_str:
        return jsonify({"error": "datetime parameter is required"}), 400
    
    config = load_config("config.json")
    
    try:
        state_to_explain = get_state_to_explain(datetime_str, config)
        logger.debug(f"found state_to_explain = {state_to_explain}")
        if not state_to_explain:
            return jsonify({"error": "No state found for the provided datetime"}), 404
        
        results = run_select_query(state_to_explain, config)
        logger.info(f"Explanations results = {results}")
        
        return jsonify({
            "stateToExplain": state_to_explain,
            "explanations": results,
        })
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    config = load_config("config.json")
    app.run(debug=True, host='0.0.0.0', port=config["explanation-engine"]["port"])
