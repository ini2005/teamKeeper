/**
 * The size object
 */
function Size() {
	this.height;
	this.width;

	this.setHeight = function(height) {
		this.height = height;
	};

	this.setWidth = function(width) {
		this.width = width;
	};
};

/**
 * The position object
 */
function Position() {
	this.x;
	this.y;

	this.setX = function(x) {
		this.x = x;
	};

	this.setY = function(y) {
		this.y = y;
	};
};

/**
 * The motion event
 */
function MotionEvent() {
	this.motionDownPosition;
	this.motionMovePositions;
	this.motionUpPosition;
	this.originalImageSize;

	this.setMotionDownPosition = function(motionDownPosition) {
		this.motionDownPosition = motionDownPosition;
	};

	this.setMotionMovePositions = function(motionMovePositions) {
		this.motionMovePositions = motionMovePositions;
	};
	
	this.pushMotionMovePositions = function(motionMovePosition) {
		if(this.motionMovePositions) {
			this.motionMovePositions.push(motionMovePosition);
		}
	};
	
	this.setMotionUpPosition = function(motionUpPosition) {
		this.motionUpPosition = motionUpPosition;
	};

	this.setOriginalImageSize = function(originalImageSize) {
		this.originalImageSize = originalImageSize;
	};
}
