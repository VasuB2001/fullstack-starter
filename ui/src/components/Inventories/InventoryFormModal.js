import * as Yup from 'yup'
import Button from '@material-ui/core/Button'
import Checkbox from '@material-ui/core/Checkbox'
import Dialog from '@material-ui/core/Dialog'
import DialogActions from '@material-ui/core/DialogActions'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import Grid from '@material-ui/core/Grid'
import moment from 'moment'
import React from 'react'
import TextField from '../Form/TextField'
import { Field, Form, Formik } from 'formik'
import { FormControlLabel, FormGroup, MenuItem } from '@material-ui/core'




class InventoryFormModal extends React.Component {
  render() {
    const inventorySchema = Yup.object().shape({
      name: Yup.string().required(),
      productType: Yup.string().required(),
      unitOfMeasurement: Yup.string().required(),
    })
    const {
      formName,
      handleDialog,
      handleInventory,
      products,
      units,
      title,
      initialValues
    } = this.props

    return (
      <Dialog
        open={this.props.isDialogOpen}
        maxWidth='sm'
        fullWidth={true}
        onClose={() => { handleDialog(false) }}
      >
        <Formik
          initialValues={initialValues}
          validationSchema={inventorySchema}
          onSubmit={values => {
            const instBestBeforeDate = moment(values.bestBeforeDate)
            handleInventory({ ...values, bestBeforeDate: instBestBeforeDate })
            handleDialog(true)
          }}>
          {helpers =>
            <Form
              noValidate
              autoComplete='off'
              id={formName}
            >
              <DialogTitle id='alert-dialog-title'>
                {`${title} Inventory`}
              </DialogTitle>
              <DialogContent>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true }}
                      name='name'
                      label='Name'
                      component={TextField}
                      required
                    />
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <Field
                      name='productType'
                      component={TextField}
                      select
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      required
                    >
                      {products.map((product) =>
                        <MenuItem key={product.id} value={product.name}>{product.name}</MenuItem>
                      )}
                    </Field>
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='description'
                      label='Description'
                      component={TextField}
                    />
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='averagePrice'
                      label='Average price'
                      component={TextField}
                      type='number'
                      onKeyPress={(event) => {
                        if (!/[0-9.]/.test(event.key)) {
                          event.preventDefault()
                        }
                      }}
                      defaultValue="0.00"
                    />
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='amount'
                      label='Amount'
                      component={TextField}
                      type='number'
                      onKeyPress={(event) => {
                        if (!/[0-9.]/.test(event.key)) {
                          event.preventDefault()
                        }
                      }}
                      defaultValue="0"
                    />
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='unitOfMeasurement'
                      label='Unit Of Measurement'
                      component={TextField}
                      select
                      required
                    >
                      {Object.entries(units).map(([key, val]) =>
                        <MenuItem key={key} value={key}>{val.name}</MenuItem>
                      )}
                    </Field>
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <Field
                      custom={{ variant: 'outlined', fullWidth: true, }}
                      name='bestBeforeDate'
                      component={TextField}
                      type='date'
                    />
                  </Grid>
                  <Grid item xs={12} sm={12}>
                    <FormGroup>
                      <FormControlLabel required control={<Checkbox />} label="Does this Expire?" />
                    </FormGroup>
                  </Grid>

                </Grid>
              </DialogContent>
              <DialogActions>
                <Button onClick={() => { handleDialog(false) }} color='secondary'>Cancel</Button>
                <Button
                  disableElevation
                  variant='contained'
                  type='submit'
                  form={formName}
                  color='secondary'
                  disabled={!helpers.dirty}>
                  Save
                </Button>
              </DialogActions>
            </Form>
          }
        </Formik>
      </Dialog>
    )
  }
}

export default InventoryFormModal