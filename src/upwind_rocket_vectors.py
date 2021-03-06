import simulation 
import csv
import numpy as np

class UpwindRocketVectors(object):
    # Initialise all parameters
    def __init__(self) :
        self.args = 0
        self.upwind_args = 0
        self.bestAngle = 0
        self.bestDistance = -1
        self.distance_array = []
        self.angle_array = []


        self.sim = simulation.Simulation()

    # Sets all the arguments 
    def set_args(self,new_args,new_upwind_args) :
        self.args = new_args
        self.args.rodAngleSigma = 0
        
        self.upwind_args = new_upwind_args
        self.correct_order()
    
    # Run the upwind rocket vector analysis
    def run_analysis(self):
        currentAngle = self.upwind_args.upwindMinAngle
        while currentAngle <= self.upwind_args.upwindMaxAngle :
            # update new value of rodAngle
            self.args.rodAngle = currentAngle

            # run simulation
            self.sim.set_args(self.args)
            simulationValue = self.sim.runSimulation()

            # get distance 
            distance = simulationValue.getResults().__dict__['lateraldistance']

            # update value if smaller
            if self.bestDistance == -1 or self.bestDistance > distance :
                self.bestDistance = distance
                self.bestAngle = currentAngle

            # store distances for possible use/testing
            self.distance_array.append(distance)
            self.angle_array.append(currentAngle)


            currentAngle += self.upwind_args.upwindStepSize

        self.print_stats()

    # Order the values by size
    def correct_order(self) :
        if self.upwind_args.upwindStepSize < 0 : 
            self.upwind_args.upwindStepSize = self.upwind_args.upwindStepSize * -1
        
        if self.upwind_args.upwindMinAngle > self.upwind_args.upwindMaxAngle :
            minValue = self.upwind_args.upwindMaxAngle
            maxValue = self.upwind_args.upwindMinAngle

            self.upwind_args.upwindMinAngle = minValue
            self.upwind_args.upwindMaxAngle = maxValue

    # Get the arguments
    def get_args(self) :
        return self.args

    # Get the upwind argument
    def get_upwind_args(self) :
        return self.upwind_args

    # Get the best angle
    def get_bestAngle(self) :
        return self.bestAngle

    # Get the best distance
    def get_bestDistance(self) :
        return self.bestDistance

    # Get the distance array
    def get_distance_array(self) :
        return self.distance_array

    # Get the angle array
    def get_angle_array(self) :
        return self.angle_array

    # Print the summary statistics
    def print_stats(self):
        if self.isWritable(self.args.outfile):
            with open(self.args.outfile, 'w',newline="\n") as file:
                writer = csv.writer(file)
                writer.writerow(["Angle","Lateral Distance"])           
                for p, q in zip(self.angle_array,self.distance_array):
                    writer.writerow([p, q])

            file.close()

    # Check a given pathway is a writeable file
    def isWritable(self,path):
        try:
            fileTest = open( path, 'w' )
            fileTest.close()
        except IOError:
            return False
        return True